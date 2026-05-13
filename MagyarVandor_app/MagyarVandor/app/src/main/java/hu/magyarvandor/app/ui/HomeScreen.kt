package hu.magyarvandor.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.magyarvandor.app.R
import hu.magyarvandor.app.domain.PlaceCategories
import hu.magyarvandor.app.ui.components.PlaceCard

/**
 * Kezdőképernyő (HomeScreen):
 * - Megjeleníti a logót és az alap információkat (felvitt helyek száma).
 * - Két gyors akciógombot ad: térkép megnyitása és admin felület.
 * - Keresőmezővel szűr a helyek között (név + leírás alapján).
 * - Kategória szűrő chip-ekkel szűr.
 * - Az eredményt kártyás GRID-ben listázza (PlaceCard komponensekkel).
 *
 * Megjegyzés:
 * - A képernyő MVVM-el működik: a HomeViewModel adja a places listát (StateFlow),
 *   a UI csak megjelenít és a felhasználói interakciókra reagál.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onOpenMap: () -> Unit,               // Navigáció: térkép képernyőre
    onOpenDetails: (Long) -> Unit,        // Navigáció: részletek képernyőre (id alapján)
    onAdmin: () -> Unit,                  // Navigáció: admin login/panel
    viewModel: HomeViewModel = hiltViewModel() // DI: ViewModel Hilt-tel
) {
    // A helyek listája a ViewModelből jön (reaktív: újrarajzol, ha változik)
    val places by viewModel.places.collectAsState()

    // UI-állapotok (csak a képernyőn belül kellenek)
    var query by remember { mutableStateOf("") }                  // keresőszöveg
    var selectedCategory by remember { mutableStateOf("Összes") } // kiválasztott kategória

    // A kategória opciók listája (egyszer rakjuk össze, ezért remember)
    val categoryOptions = remember { listOf("Összes") + PlaceCategories.all }

    /**
     * Szűrt lista előállítása:
     * 1) kategória szerint (ha nem "Összes")
     * 2) keresőszöveg szerint (név vagy leírás tartalmazza, kis/nagybetűtől függetlenül)
     *
     * Megjegyzés:
     * - asSequence() használat: kicsit hatékonyabb láncolt filtereknél, majd a végén toList().
     */
    val filtered = places
        .asSequence()
        .filter { selectedCategory == "Összes" || it.category == selectedCategory }
        .filter {
            if (query.isBlank()) true
            else it.name.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
        }
        .toList()

    /**
     * Scaffold:
     * - topBar: fix fejléc (app név)
     * - contentWindowInsets = WindowInsets(0): mi kezeljük a paddinget (edge-to-edge)
     */
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { Text("MagyarVándor") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { innerPadding ->
        /**
         * Surface:
         * - kitölti a képernyőt
         * - alkalmazza a Scaffold belső paddingjét
         * - navigationBarsPadding(): ne lógjon a tartalom a rendszer navigációs sáv alá
         */
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                // ===== HERO: logó + "Kezdő" + helyek száma =====
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_mv),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Kezdő",
                            style = MaterialTheme.typography.titleLarge
                        )
                        Text(
                            text = "Felvitt helyek: ${places.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // ===== AKCIÓ GOMBOK: térkép + admin =====
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = onOpenMap,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Map, contentDescription = null)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Térkép")
                    }

                    Button(
                        onClick = onAdmin,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.AdminPanelSettings, contentDescription = null)
                        Spacer(modifier = Modifier.size(8.dp))
                        Text("Admin")
                    }
                }

                // ===== KERESŐ: név + leírás alapján szűr =====
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Keresés helyek között") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                )

                // ===== KATEGÓRIA CHIP-EK: gyors szűrés =====
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categoryOptions) { cat ->
                        val selected = cat == selectedCategory
                        AssistChip(
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) },
                            colors = AssistChipDefaults.assistChipColors(
                                // a kijelölt chip enyhén kiemelt háttérrel jelenik meg
                                containerColor = if (selected) {
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                                } else {
                                    MaterialTheme.colorScheme.surfaceContainer
                                }
                            )
                        )
                    }
                }

                // ===== FEJLÉC SOR: "Helyek" + darabszám =====
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Helyek",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${filtered.size} db",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // ===== ÜRES ÁLLAPOT: ha nincs találat a szűrésre =====
                if (filtered.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("Nincs találat", style = MaterialTheme.typography.titleMedium)
                            Text(
                                "Próbáld meg másik kategóriával vagy keresőszóval.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Fontos: ha nincs találat, ne rajzoljuk ki a gridet.
                    return@Surface
                }

                // ===== GRID: a szűrt helyek kártyákban =====
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(minSize = 170.dp),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filtered, key = { it.id }) { p ->
                        PlaceCard(
                            name = p.name,
                            category = p.category,
                            onClick = { onOpenDetails(p.id) }
                        )
                    }
                }
            }
        }
    }
}