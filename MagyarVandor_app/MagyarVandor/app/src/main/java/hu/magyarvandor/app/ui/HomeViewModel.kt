package hu.magyarvandor.app.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.magyarvandor.app.data.PlaceRepository
import hu.magyarvandor.app.domain.Place
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Kezdőképernyő ViewModel:
 * - helyek listáját adja a UI-nak
 * - törlést delegál a repository-nak
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: PlaceRepository
) : ViewModel() {

    val places: StateFlow<List<Place>> = repo.places

    fun delete(id: Long) {
        repo.deletePlace(id)
    }
}