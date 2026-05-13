package hu.magyarvandor.app.ui.util

import hu.magyarvandor.app.R

/**
 * Kategória -> drawable ikon leképzés.
 * Azért van külön fájlban, hogy ne legyen duplikálva több képernyőben.
 */
fun categoryIconRes(category: String): Int = when (category) {
    "Vár" -> R.drawable.ic_cat_castle
    "Templom" -> R.drawable.ic_cat_church
    "Kilátó" -> R.drawable.ic_cat_lookout
    "Múzeum" -> R.drawable.ic_cat_museum
    "Szobor" -> R.drawable.ic_cat_statue
    else -> R.drawable.ic_cat_other
}