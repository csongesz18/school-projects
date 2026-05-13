package hu.magyarvandor.app.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.magyarvandor.app.data.AdminSession
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

// ViewModel az admin login logikához (MVVM)
@HiltViewModel
class AdminLoginViewModel @Inject constructor(
    private val adminSession: AdminSession // session kezelés (bejelentkezés állapot)
) : ViewModel() {

    // belső mutable state
    private val _state = MutableStateFlow(AdminLoginUiState())

    // külső olvasható state a UI számára
    val state: StateFlow<AdminLoginUiState> = _state

    // felhasználónév változás kezelése
    fun onUsernameChange(value: String) {
        _state.update { it.copy(username = value, error = null) }
    }

    // jelszó változás kezelése
    fun onPasswordChange(value: String) {
        _state.update { it.copy(password = value, error = null) }
    }

    // login logika (jelenleg fix "a" / "a")
    fun login() {
        val s = _state.value

        if (s.username == "admin" && s.password == "Admin1234") {
            adminSession.login() // session beállítása
            _state.update { it.copy(isLoggedIn = true, error = null) }
        } else {
            _state.update {
                it.copy(error = "Hibás felhasználónév vagy jelszó")
            }
        }
    }

    // login flag reset (hogy ne fusson újra a navigáció)
    fun consumeLogin() {
        _state.update { it.copy(isLoggedIn = false) }
    }
}