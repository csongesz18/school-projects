package hu.magyarvandor.app.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.magyarvandor.app.data.AdminSession
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

// ViewModel az AdminGuard-hoz
// Feladata: elérhetővé tenni az admin státuszt a UI számára
@HiltViewModel
class AdminGuardViewModel @Inject constructor(
    adminSession: AdminSession // DI-ből kapjuk az aktuális admin sessiont
) : ViewModel() {

    // Az admin állapot (true = be van jelentkezve admin, false = nincs)
    // Ezt figyeli az AdminGuard composable
    val isAdmin: StateFlow<Boolean> = adminSession.isAdmin
}