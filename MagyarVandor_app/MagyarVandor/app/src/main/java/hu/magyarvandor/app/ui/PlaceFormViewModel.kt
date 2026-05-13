package hu.magyarvandor.app.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.magyarvandor.app.data.FirebaseImageUploader
import hu.magyarvandor.app.data.PlaceRepository
import hu.magyarvandor.app.domain.Place
import hu.magyarvandor.app.ui.model.ImageSource
import hu.magyarvandor.app.ui.model.PlaceFormUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * PlaceFormViewModel:
 * - a PlaceFormScreen állapotát (UiState) kezeli
 * - szerkesztésnél betölti a place adatokat + a hozzá tartozó képeket
 * - mentésnél a Place + képlista mentése a repository-n keresztül történik
 */
@HiltViewModel
class PlaceFormViewModel @Inject constructor(
    private val repo: PlaceRepository,
    private val imageUploader: FirebaseImageUploader,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Ha van placeId a route-ban, akkor szerkesztés mód
    private val placeId: Long? =
        savedStateHandle.get<String>("placeId")?.toLongOrNull()

    private val _state = MutableStateFlow(PlaceFormUiState())
    val state: StateFlow<PlaceFormUiState> = _state

    init {
        // Szerkesztés esetén: először az alap place adatokat tesszük be a formba,
        // majd külön betöltjük a place_images táblából a képeket.
        if (placeId != null) {
            val place = repo.places.value.find { it.id == placeId }
            if (place != null) {
                _state.value = PlaceFormUiState(
                    name = place.name,
                    description = place.description,
                    history = place.history,
                    latText = place.latitude.toString(),
                    lonText = place.longitude.toString(),
                    category = place.category,

                    // képek külön jönnek DB-ből
                    images = emptyList(),

                    imageSource = ImageSource.PICKER,
                    imageUri = null,
                    imageUrl = ""
                )
            }

            // Képek betöltése a place_images táblából
            viewModelScope.launch {
                val imgs = repo.getImagesForPlace(placeId)
                _state.update { it.copy(images = imgs) }

                // Visszafelé kompatibilitás:
                // ha nincs kép a táblában, de a régi imageUri mezőben még van 1 db.
                if (imgs.isEmpty()) {
                    val fallback = place?.imageUri
                    if (!fallback.isNullOrBlank()) {
                        _state.update { s -> s.copy(images = listOf(fallback)) }
                    }
                }
            }
        }
    }

    fun onNameChange(value: String) {
        _state.update { it.copy(name = value) }
    }

    fun onDescriptionChange(value: String) {
        _state.update { it.copy(description = value) }
    }

    fun onHistoryChange(value: String) {
        _state.update { it.copy(history = value) }
    }

    fun onLatChange(value: String) {
        _state.update { it.copy(latText = value) }
    }

    fun onLonChange(value: String) {
        _state.update { it.copy(lonText = value) }
    }

    fun onCategoryChange(value: String) {
        _state.update { it.copy(category = value) }
    }

    fun onImageSourceChange(value: ImageSource) {
        _state.update { it.copy(imageSource = value) }
    }

    fun onImageUrlChange(value: String) {
        _state.update { it.copy(imageUrl = value) }
    }

    // Pickerből kiválasztott kép hozzáadása (duplikáció nélkül)
    fun addPickedImage(uri: String) {
        val v = uri.trim()
        if (v.isBlank()) return

        _state.update { s ->
            if (s.images.contains(v)) s else s.copy(images = s.images + v)
        }
    }

    // URL mezőből kép hozzáadása + mező ürítése
    fun addUrlImage() {
        _state.update { s ->
            val url = s.imageUrl.trim()
            if (url.isBlank()) return@update s
            if (!url.startsWith("http")) return@update s

            if (s.images.contains(url)) {
                s.copy(imageUrl = "")
            } else {
                s.copy(images = s.images + url, imageUrl = "")
            }
        }
    }

    // Kép törlése a listából
    fun removeImage(value: String) {
        _state.update { s -> s.copy(images = s.images.filterNot { it == value }) }
    }

    // Mentés: Place + teljes képlista mentése repository-n keresztül
    // Mentés: Place + teljes képlista mentése repository-n keresztül
    suspend fun save(lat: Double, lon: Double): Boolean = withContext(Dispatchers.IO) {
        val s = _state.value
        if (s.name.isBlank() || s.description.isBlank() || s.history.isBlank()) return@withContext false

        _state.update { it.copy(isSaving = true) }

        try {
            val uploadedImages = s.images.map { image ->
                when {
                    image.startsWith("http") -> image
                    image.startsWith("content://") -> imageUploader.uploadImage(image)
                    else -> image
                }
            }

            val firstImage = uploadedImages.firstOrNull()

            val place = Place(
                id = placeId ?: 0L,
                name = s.name.trim(),
                description = s.description.trim(),
                history = s.history.trim(),
                latitude = lat,
                longitude = lon,
                category = s.category,
                imageUri = firstImage
            )

            if (placeId == null) repo.addPlace(place, uploadedImages)
            else repo.updatePlace(place, uploadedImages)

            true
        } catch (e: Exception) {
            false
        } finally {
            _state.update { it.copy(isSaving = false) }
        }
    }
}