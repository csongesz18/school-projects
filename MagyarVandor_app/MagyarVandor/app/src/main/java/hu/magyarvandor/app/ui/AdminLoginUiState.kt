package hu.magyarvandor.app.ui

// UI állapot adatosztály a login képernyőhöz
data class AdminLoginUiState(
    val username: String = "",     // felhasználónév mező
    val password: String = "",     // jelszó mező
    val error: String? = null,     // hibaüzenet (pl. rossz login)
    val isLoggedIn: Boolean = false // sikeres belépés flag (navigációhoz)
)