package hu.magyarvandor.app.domain

// Ez az objektum tartalmazza az alkalmazásban használható kategóriákat.
// Ezek jelennek meg:
// - a legördülő listákban (PlaceFormScreen)
// - a szűrőkben (HomeScreen, MapScreen, Admin)
// - ikon hozzárendeléshez is ezt használjuk
object PlaceCategories {

    // Az összes választható kategória listája
    val all: List<String> = listOf(
        "Vár",       // történelmi várak
        "Templom",   // vallási épületek
        "Múzeum",    // kiállítóhelyek
        "Kilátó",    // panoráma pontok
        "Szobor",    // emlékművek, szobrok
        "Egyéb"      // minden más kategória
    )
}