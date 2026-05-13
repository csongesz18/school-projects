package hu.magyarvandor.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.* // (jelenleg nem használod, de maradhat)
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Church
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Museum
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import hu.magyarvandor.app.domain.PlaceCategories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPlacesScreen(
    onBack: () -> Unit,                 // visszalépés a navigációban
    onEdit: (Long) -> Unit,             // szerkesztés képernyőre navigálás (placeId-val)
    viewModel: HomeViewModel = androidx.hilt.navigation.compose.hiltViewModel() // Hilt ViewModel injektálás
) {
    // A helyek listája a ViewModelből (StateFlow -> Compose state)
    val places by viewModel.places.collectAsState()

    // Törlés megerősítő dialog állapota (melyik elemet akarjuk törölni)
    var placeToDeleteId by remember { mutableStateOf<Long?>(null) }
    var placeToDeleteName by remember { mutableStateOf<String?>(null) }

    // ===== FILTER (kategória) =====
    // dropdown nyitva/zárva
    var categoryExpanded by remember { mutableStateOf(false) }
    // kiválasztott kategória
    var selectedCategory by remember { mutableStateOf("Összes") }
    // választható opciók: "Összes" + a fix kategórialistád
    val categoryOptions = listOf("Összes") + PlaceCategories.all

    // A lista szűrése a kiválasztott kategória alapján
    val filteredPlaces =
        if (selectedCategory == "Összes") places
        else places.filter { it.category == selectedCategory }

    // Scaffold: topbar + tartalom
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Helyek szerkesztése") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Vissza")
                    }
                }
            )
        }
    ) { innerPadding ->

        // ===== TÖRLÉS MEGERŐSÍTÉS (AlertDialog) =====
        // Ha placeToDeleteId != null, akkor megjelenítjük a dialogot.
        if (placeToDeleteId != null) {
            AlertDialog(
                onDismissRequest = {
                    // Dialog bezárása (állapot nullázása)
                    placeToDeleteId = null
                    placeToDeleteName = null
                },
                title = { Text("Törlés megerősítése") },
                text = { Text("Biztosan törlöd ezt a helyet?\n\n${placeToDeleteName.orEmpty()}") },
                confirmButton = {
                    TextButton(onClick = {
                        // tényleges törlés a ViewModelen keresztül
                        viewModel.delete(placeToDeleteId!!)
                        // dialog bezárása
                        placeToDeleteId = null
                        placeToDeleteName = null
                    }) { Text("Törlés") }
                },
                dismissButton = {
                    TextButton(onClick = {
                        // mégse: csak bezárjuk
                        placeToDeleteId = null
                        placeToDeleteName = null
                    }) { Text("Mégse") }
                }
            )
        }

        // ===== FŐ TARTALOM =====
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Scaffold belső paddingje (topbar miatt)
                .padding(16.dp)
        ) {

            // ===== HEADER KÁRTYA =====
            // Gyors összefoglaló: mennyi elem látszik / összesen mennyi van
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Helyek kezelése", style = MaterialTheme.typography.titleMedium)
                    Text("Megjelenítve: ${filteredPlaces.size} / ${places.size}")
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ===== KATEGÓRIA FILTER (ExposedDropdownMenu) =====
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = !categoryExpanded }
            ) {
                // A mező, amire kattintva lenyílik a menü
                OutlinedTextField(
                    value = selectedCategory,
                    onValueChange = {}, // readOnly, ezért nem írható
                    readOnly = true,
                    label = { Text("Kategória") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                    },
                    modifier = Modifier
                        .menuAnchor() // fontos az ExposedDropdownMenuBox-hoz
                        .fillMaxWidth()
                )

                // A lenyíló menü elemei
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    categoryOptions.forEach { cat ->
                        DropdownMenuItem(
                            text = {
                                // ikon + szöveg egy sorban
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = categoryIcon(cat),
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(cat)
                                }
                            },
                            onClick = {
                                selectedCategory = cat
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // ===== LISTA (LazyColumn) =====
            // A szűrt helyek listája kártyákban, műveleti ikonokkal.
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredPlaces) { p ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(20.dp)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            // ===== KATEGÓRIA IKON =====
                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = categoryIcon(p.category),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            // ===== SZÖVEGEK =====
                            Column(
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = p.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    text = p.category,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }

                            // ===== AKCIÓK =====
                            // Szerkesztés: placeId átadása a navigációnak
                            IconButton(onClick = { onEdit(p.id) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Szerkesztés")
                            }

                            // Törlés: csak dialogot nyitunk (nem törlünk azonnal)
                            IconButton(onClick = {
                                placeToDeleteId = p.id
                                placeToDeleteName = p.name
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Törlés")
                            }
                        }
                    }
                }
            }
        }
    }
}