package hu.magyarvandor.app.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.magyarvandor.app.data.PlaceRepository
import hu.magyarvandor.app.domain.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailsViewModel @Inject constructor(
    private val repo: PlaceRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Route-ból érkező place id
    private val placeId: Long =
        savedStateHandle.get<String>("id")?.toLongOrNull() ?: 0L

    // Kiválasztott hely adatai
    private val _place = MutableStateFlow<Place?>(null)
    val place: StateFlow<Place?> = _place.asStateFlow()

    // Helyhez tartozó képek
    private val _images = MutableStateFlow<List<String>>(emptyList())
    val images: StateFlow<List<String>> = _images.asStateFlow()

    init {
        // Place betöltése
        viewModelScope.launch {
            repo.places.collect { list ->
                _place.value = list.find { it.id == placeId }
            }
        }

        // Képek betöltése
        viewModelScope.launch {
            _images.value = repo.getImagesForPlace(placeId)
        }
    }
}