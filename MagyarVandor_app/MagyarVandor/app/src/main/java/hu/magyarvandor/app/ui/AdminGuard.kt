package hu.magyarvandor.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import hu.magyarvandor.app.navigation.Routes

// Ez a composable egy "védőréteg" az admin oldalakhoz
// Csak akkor engedi megjelenni a tartalmat, ha a user admin
@Composable
fun AdminGuard(
    navController: NavHostController,
    popUpToRoute: String, // ide navigál vissza, ha nem admin
    content: @Composable () -> Unit // a védett tartalom
) {
    // ViewModel, ami tárolja az admin állapotot
    val guard: AdminGuardViewModel = hiltViewModel()

    // Figyeljük, hogy be van-e jelentkezve admin
    val isAdmin by guard.isAdmin.collectAsState()

    // Ha változik az admin állapot
    LaunchedEffect(isAdmin) {
        if (!isAdmin) {
            // Ha NEM admin → visszadob login screenre
            navController.navigate(Routes.ADMIN_LOGIN) {
                popUpTo(popUpToRoute) { inclusive = true } // törli az előző screeneket
            }
        }
    }

    // Ha admin → megjelenítjük a tartalmat
    if (isAdmin) {
        content()
    }
}