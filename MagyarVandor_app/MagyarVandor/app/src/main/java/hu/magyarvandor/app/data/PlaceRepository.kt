package hu.magyarvandor.app.data

import hu.magyarvandor.app.domain.Place
import kotlinx.coroutines.flow.StateFlow

/**
 * Repository interface a helyek (Place) kezelésére.
 * Az MVVM architektúrában ez biztosítja az adatforrást a ViewModel számára.
 */
interface PlaceRepository {

    /**
     * A helyek listája folyamatosan figyelhető StateFlow-ként.
     */
    val places: StateFlow<List<Place>>

    /**
     * Új hely hozzáadása a hozzá tartozó képekkel.
     */
    fun addPlace(place: Place, images: List<String>)

    /**
     * Meglévő hely frissítése a hozzá tartozó képekkel.
     */
    fun updatePlace(place: Place, images: List<String>)

    /**
     * Egy adott helyhez tartozó képek lekérdezése.
     */
    suspend fun getImagesForPlace(placeId: Long): List<String>

    /**
     * Hely törlése azonosító alapján.
     */
    fun deletePlace(id: Long)
}