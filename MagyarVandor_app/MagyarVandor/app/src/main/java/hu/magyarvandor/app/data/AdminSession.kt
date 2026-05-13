package hu.magyarvandor.app.data

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Egyszerű session kezelés az admin jogosultsághoz.
 * StateFlow segítségével figyelhető, hogy a felhasználó be van-e jelentkezve.
 */
@Singleton
class AdminSession @Inject constructor() {

    // Belső állapot (írható)
    private val _isAdmin = kotlinx.coroutines.flow.MutableStateFlow(false)

    // Külső állapot (csak olvasható)
    val isAdmin: kotlinx.coroutines.flow.StateFlow<Boolean> = _isAdmin

    /** Admin bejelentkezés */
    fun login() { _isAdmin.value = true }

    /** Admin kijelentkezés */
    fun logout() { _isAdmin.value = false }
}