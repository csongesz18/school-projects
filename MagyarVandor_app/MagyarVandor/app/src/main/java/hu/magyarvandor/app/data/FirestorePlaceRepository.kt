package hu.magyarvandor.app.data

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import hu.magyarvandor.app.domain.Place
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * Firestore alapú repository.
 * Az adatok online adatbázisban tárolódnak,
 * és változás esetén automatikusan frissülnek.
 */
@Singleton
class FirestorePlaceRepository @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : PlaceRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // A helyek listája, amit a ViewModel figyel
    private val _places = MutableStateFlow<List<Place>>(emptyList())
    override val places: StateFlow<List<Place>> = _places

    init {
        // Firestore figyelése: ha változik az adat, frissítjük a listát
        firestore.collection(COLLECTION_NAME)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    android.util.Log.e("FIRESTORE", "Snapshot hiba", error)
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(FirestorePlaceDto::class.java)?.toDomain()
                }?.sortedByDescending { it.id } ?: emptyList()

                _places.value = list
            }
    }

    /**
     * Új hely hozzáadása Firestore-ba.
     */
    override fun addPlace(place: Place, images: List<String>) {
        scope.launch {
            val newId = generatePlaceId()
            val placeWithId = place.copy(
                id = newId,
                imageUri = images.firstOrNull()
            )

            firestore.collection(COLLECTION_NAME)
                .document(newId.toString())
                .set(placeWithId.toFirestoreDto(images))
                .await()
        }
    }

    /**
     * Meglévő hely frissítése.
     * A törölt képeket Storage-ból is eltávolítjuk.
     */
    override fun updatePlace(place: Place, images: List<String>) {
        if (place.id == 0L) return

        scope.launch {
            val docRef = firestore.collection(COLLECTION_NAME)
                .document(place.id.toString())

            val oldDto = docRef.get().await().toObject(FirestorePlaceDto::class.java)
            val oldImages = oldDto?.images ?: emptyList()

            // Ezek a képek már nem maradnak meg a helyhez
            val removedImages = oldImages.filterNot { it in images }

            removedImages.forEach { imageUrl ->
                deleteImageFromStorageIfManaged(imageUrl)
            }

            val updatedPlace = place.copy(
                imageUri = images.firstOrNull()
            )

            docRef.set(updatedPlace.toFirestoreDto(images)).await()
        }
    }

    /**
     * Egy helyhez tartozó képek lekérdezése.
     */
    override suspend fun getImagesForPlace(placeId: Long): List<String> =
        suspendCancellableCoroutine { continuation ->
            firestore.collection(COLLECTION_NAME)
                .document(placeId.toString())
                .get()
                .addOnSuccessListener { snapshot ->
                    val dto = snapshot.toObject(FirestorePlaceDto::class.java)
                    continuation.resume(dto?.images ?: emptyList())
                }
                .addOnFailureListener {
                    continuation.resume(emptyList())
                }
        }

    /**
     * Hely törlése az adatbázisból.
     * A hozzá tartozó Storage képeket is töröljük.
     */
    override fun deletePlace(id: Long) {
        scope.launch {
            val docRef = firestore.collection(COLLECTION_NAME)
                .document(id.toString())

            val dto = docRef.get().await().toObject(FirestorePlaceDto::class.java)
            val images = dto?.images ?: emptyList()

            images.forEach { imageUrl ->
                deleteImageFromStorageIfManaged(imageUrl)
            }

            docRef.delete().await()
        }
    }

    /**
     * Firebase Storage-ból törli a képet, ha a mi bucketünkből származik.
     * Külső URL-t nem bántunk.
     */
    private suspend fun deleteImageFromStorageIfManaged(imageUrl: String) {
        if (!isFirebaseStorageUrl(imageUrl)) return

        try {
            storage.getReferenceFromUrl(imageUrl)
                .delete()
                .await()
        } catch (e: Exception) {
            android.util.Log.e("STORAGE_DELETE", "Nem sikerült törölni: $imageUrl", e)
        }
    }

    /**
     * Eldönti, hogy a kép a Firebase Storage-ból származik-e.
     */
    private fun isFirebaseStorageUrl(url: String): Boolean {
        return url.startsWith("https://firebasestorage.googleapis.com/") ||
                url.startsWith("gs://") ||
                url.contains("magyarvandor.firebasestorage.app")
    }

    /**
     * Egyszerű ID generálás időbélyeg alapján.
     */
    private fun generatePlaceId(): Long {
        return System.currentTimeMillis()
    }

    companion object {
        private const val COLLECTION_NAME = "places"
    }
}