package hu.magyarvandor.app.navigation

import android.net.Uri

// Az alkalmazás navigációs útvonalait (route-ok) tartalmazza egy helyen
// Így könnyen karbantartható és nem kell mindenhol stringeket írni
object Routes {

    // Kezdőképernyő
    const val HOME = "home"

    // Térkép képernyő (alap)
    const val MAP = "map"

    // Térkép képernyő fókuszált nézettel (adott koordinátára ugrik)
    const val MAP_FOCUS = "map?lat={lat}&lon={lon}&name={name}"

    // Részletek képernyő paraméterrel (hely ID alapján)
    const val DETAILS_WITH_ID = "details/{id}"

    // Dinamikus route generálása adott ID-val
    fun details(id: Long) = "details/$id"

    // Admin bejelentkezés képernyő
    const val ADMIN_LOGIN = "admin_login"

    // Admin fő panel
    const val ADMIN_PANEL = "admin_panel"

    // Új hely hozzáadása
    const val PLACE_FORM = "place_form"

    // Hely szerkesztése ID alapján
    const val PLACE_FORM_WITH_ID = "place_form/{placeId}"

    // Dinamikus route szerkesztéshez
    fun placeForm(id: Long) = "place_form/$id"

    // Admin hely lista
    const val ADMIN_PLACES = "admin_places"

    // Térkép route generálása konkrét koordinátával és névvel
    fun mapFocus(lat: Double, lon: Double, name: String): String {
        // A név encode-olva van, hogy ne törje el az URL-t (pl. szóközök miatt)
        return "map?lat=$lat&lon=$lon&name=${Uri.encode(name)}"
    }
}