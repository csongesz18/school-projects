package hu.magyarvandor.app.data

import hu.magyarvandor.app.domain.Place

/**
 * Firestore adatmodell.
 * Ez a struktúra kerül eltárolásra az online adatbázisban.
 */
data class FirestorePlaceDto(
    val id: Long = 0L,
    val name: String = "",
    val description: String = "",
    val history: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val imageUri: String? = null,
    val category: String = "Egyéb",
    val images: List<String> = emptyList()
)

/**
 * Firestore → Domain átalakítás.
 * Az adatbázisból érkező adatot alakítjuk át az app által használt modellre.
 */
fun FirestorePlaceDto.toDomain(): Place =
    Place(
        id = id,
        name = name,
        description = description,
        history = history,
        latitude = latitude,
        longitude = longitude,
        imageUri = imageUri,
        category = category
    )

/**
 * Domain → Firestore átalakítás.
 * A mentéshez alakítjuk át az app adatát Firestore formátumra.
 */
fun Place.toFirestoreDto(images: List<String>): FirestorePlaceDto =
    FirestorePlaceDto(
        id = id,
        name = name,
        description = description,
        history = history,
        latitude = latitude,
        longitude = longitude,
        imageUri = imageUri,
        category = category,
        images = images
    )