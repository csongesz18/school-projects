package hu.magyarvandor.app.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

// Ez a függvény visszaadja a megfelelő ikont a kategória alapján.
// Így nem kell minden screenben külön when-t írni → újrahasznosítható megoldás.
fun categoryIcon(category: String): ImageVector = when (category) {

    // Speciális eset: összes kategória (pl. szűrőnél)
    "Összes" -> Icons.Default.List

    // Kategóriákhoz tartozó ikonok
    "Vár" -> Icons.Default.AccountBalance
    "Templom" -> Icons.Default.Church
    "Kilátó" -> Icons.Default.Visibility
    "Múzeum" -> Icons.Default.Museum
    "Szobor" -> Icons.Default.Person

    // Alapértelmezett ikon, ha nincs külön kezelve
    else -> Icons.Default.Place
}