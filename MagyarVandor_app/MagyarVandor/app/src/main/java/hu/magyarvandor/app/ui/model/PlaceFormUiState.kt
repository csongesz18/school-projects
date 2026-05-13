package hu.magyarvandor.app.ui.model

// A PlaceFormScreen teljes állapota egyben.
// Ezt a ViewModel tárolja StateFlow-ban, és a UI ebből rajzol.
data class PlaceFormUiState(
    // Alap mezők
    val name: String = "",
    val description: String = "",
    val history: String = "",
    val latText: String = "",
    val lonText: String = "",
    val category: String = "Egyéb",

    // Kép hozzáadás módja + ideiglenes mezők a felvitelhez
    val imageSource: ImageSource = ImageSource.PICKER,
    val imageUri: String? = null, // régi kompatibilitás / átmeneti mező (nem a listás tárolás)
    val imageUrl: String = "",    // URL módban ide írjuk be a linket

    // Több kép támogatása: a ténylegesen hozzáadott képek listája
    val images: List<String> = emptyList(),

    // Mentés közben true, ilyenkor töltő jelzés jelenhet meg
    val isSaving: Boolean = false
)