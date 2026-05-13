package hu.magyarvandor.app.domain

/**
 * Domain réteghez tartozó adatmodell (üzleti logika szint).
 * Ez reprezentál egy "helyet" az alkalmazásban.
 */
data class Place(

    // Egyedi azonosító (Room automatikusan generálja új elemnél)
    val id: Long = 0L,

    // A hely neve (pl. "Budai vár")
    val name: String,

    // Rövid leírás (lista nézethez / gyors infóhoz)
    val description: String,

    // Hosszabb, részletes történeti leírás
    val history: String = "",

    // Földrajzi koordináták (térképes megjelenítéshez)
    val latitude: Double,
    val longitude: Double,

    // Visszafelé kompatibilitás miatt 1 kép (régi megoldás)
    // Az új rendszer már több képet kezel külön táblában
    val imageUri: String? = null,

    // Kategória (pl. Vár, Templom, Múzeum stb.)
    val category: String = "Egyéb"
)