package hu.magyarvandor.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import hu.magyarvandor.app.R
import hu.magyarvandor.app.ui.components.CategoryFilter
import hu.magyarvandor.app.ui.utils.getMarkerIcon

/**
 * Térkép képernyő:
 * - GoogleMap megjelenítése egy alap (Magyarország) kamerával
 * - kategória szerinti szűrés (CategoryFilter komponenssel)
 * - helyek markerrel kirajzolása (egyedi kategória ikonokkal)
 * - opcionális fókusz: ha kaptunk lat/lon-t, rázoomolunk a megadott pontra
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    onBack: () -> Unit,
    onOpenDetails: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
    focusLat: Double? = null,
    focusLon: Double? = null,
    focusName: String? = null // jelenleg csak route miatt jön, itt nem használjuk direktben
) {
    // A helyek listája a HomeViewModelből (Room -> Flow/StateFlow -> Compose state)
    val places by viewModel.places.collectAsState()

    // Context kell: map style betöltéshez + marker ikon bitmap készítéshez
    val context = LocalContext.current

    // Alap kamera pozíció: Magyarország közepe (induláskor innen zoomolunk ki)
    val huCenter = LatLng(47.1625, 19.5033)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(huCenter, 7f)
    }

    // Kiválasztott kategória a filterhez
    var selectedCategory by remember { mutableStateOf("Összes") }

    // Szűrt lista: "Összes" esetén minden, egyébként csak a kategóriába tartozók
    val filteredPlaces =
        if (selectedCategory == "Összes") places
        else places.filter { it.category == selectedCategory }

    // Egyedi map style (raw/map_style.json), hogy „vagányabb” legyen a térkép kinézete
    val mapStyle = remember {
        MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
    }

    /**
     * Ha fókusz koordinátát kaptunk (PlaceDetails -> "Mutasd a térképen"),
     * akkor animáltan rázoomolunk a helyre.
     *
     * Megjegyzés: a focusName itt nincs felhasználva, a MapScreen csak a lat/lon-ra épít.
     */
    LaunchedEffect(focusLat, focusLon) {
        if (focusLat != null && focusLon != null) {
            val target = LatLng(focusLat, focusLon)
            cameraPositionState.animate(
                update = CameraUpdateFactory.newLatLngZoom(target, 15f)
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Térkép") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Vissza")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // ===== KATEGÓRIA SZŰRŐ =====
            // Külön komponens (újrafelhasználható), ikonokkal a dropdownban.
            CategoryFilter(
                selectedCategory = selectedCategory,
                onCategorySelected = { selectedCategory = it }
            )

            // ===== GOOGLE MAP =====
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties = MapProperties(mapStyleOptions = mapStyle)
            ) {
                // ===== MARKEREK =====
                // Minden hely kirajzolása markerrel. A marker ikon kategória alapján változik.
                filteredPlaces.forEach { p ->
                    Marker(
                        state = MarkerState(position = LatLng(p.latitude, p.longitude)),
                        title = p.name,
                        snippet = p.description,
                        icon = getMarkerIcon(context, p.category),
                        onClick = {
                            // Marker kattintás: részletek megnyitása (saját képernyő)
                            onOpenDetails(p.id)
                            true
                        }
                    )
                }
            }
        }
    }
}