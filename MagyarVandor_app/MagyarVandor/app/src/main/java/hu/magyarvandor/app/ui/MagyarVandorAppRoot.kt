package hu.magyarvandor.app.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import hu.magyarvandor.app.navigation.Routes

/**
 * Az alkalmazás navigációs gyökere (NavHost).
 * Itt vannak definiálva a képernyők (route-ok) és az átjárás közöttük.
 */
@Composable
fun MagyarVandorAppRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {

        // ===== KEZDŐKÉPERNYŐ =====
        composable(Routes.HOME) {
            HomeScreen(
                onOpenMap = { navController.navigate(Routes.MAP) },
                onOpenDetails = { id -> navController.navigate(Routes.details(id)) },
                onAdmin = { navController.navigate(Routes.ADMIN_LOGIN) }
            )
        }

        // ===== TÉRKÉP (fókuszált pontra) =====
        composable(
            Routes.MAP_FOCUS,
            arguments = listOf(
                navArgument("lat") { type = NavType.StringType; defaultValue = "" },
                navArgument("lon") { type = NavType.StringType; defaultValue = "" },
                navArgument("name") { type = NavType.StringType; defaultValue = "" }
            )
        ) { backStackEntry ->
            val lat = backStackEntry.arguments?.getString("lat")?.toDoubleOrNull()
            val lon = backStackEntry.arguments?.getString("lon")?.toDoubleOrNull()
            val name = backStackEntry.arguments?.getString("name")

            MapScreen(
                onBack = { navController.popBackStack() },
                onOpenDetails = { id -> navController.navigate(Routes.details(id)) },
                focusLat = lat,
                focusLon = lon,
                focusName = name
            )
        }

        // ===== TÉRKÉP (alap) =====
        composable(Routes.MAP) {
            MapScreen(
                onBack = { navController.popBackStack() },
                onOpenDetails = { id -> navController.navigate(Routes.details(id)) }
            )
        }

        // ===== HELY RÉSZLETEK =====
        composable(Routes.DETAILS_WITH_ID) {
            PlaceDetailsScreen(
                onBack = { navController.popBackStack() },
                onOpenMapToPlace = { lat: Double, lon: Double, name: String ->
                    navController.navigate(Routes.mapFocus(lat, lon, name))
                }
            )
        }

        // ===== ADMIN LOGIN =====
        composable(Routes.ADMIN_LOGIN) {
            AdminLoginScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = { navController.navigate(Routes.ADMIN_PANEL) }
            )
        }

        // ===== ADMIN PANEL (védelemmel) =====
        composable(Routes.ADMIN_PANEL) {
            AdminGuard(navController, Routes.ADMIN_PANEL) {
                AdminPanelScreen(
                    onAddPlace = { navController.navigate(Routes.PLACE_FORM) },
                    onEditPlaces = { navController.navigate(Routes.ADMIN_PLACES) },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        // ===== ÚJ HELY =====
        composable(Routes.PLACE_FORM) {
            AdminGuard(navController, Routes.PLACE_FORM) {
                PlaceFormScreen(onBack = { navController.popBackStack() })
            }
        }

        // ===== HELYEK LISTÁJA (ADMIN) =====
        composable(Routes.ADMIN_PLACES) {
            AdminGuard(navController, Routes.ADMIN_PLACES) {
                AdminPlacesScreen(
                    onBack = { navController.popBackStack() },
                    onEdit = { id -> navController.navigate(Routes.placeForm(id)) }
                )
            }
        }

        // ===== HELY SZERKESZTÉS =====
        composable(Routes.PLACE_FORM_WITH_ID) {
            AdminGuard(navController, Routes.PLACE_FORM_WITH_ID) {
                PlaceFormScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}