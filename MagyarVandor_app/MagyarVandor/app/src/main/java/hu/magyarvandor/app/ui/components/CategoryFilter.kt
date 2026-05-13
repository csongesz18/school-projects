package hu.magyarvandor.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.magyarvandor.app.domain.PlaceCategories

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFilter(
    selectedCategory: String,              // jelenleg kiválasztott kategória
    onCategorySelected: (String) -> Unit   // callback, ha új kategóriát választ a user
) {
    // dropdown nyitott/zárt állapota
    var expanded by remember { mutableStateOf(false) }

    // lista: "Összes" + összes kategória
    val filterOptions = listOf("Összes") + PlaceCategories.all

    // külső kártya a szép UI miatt
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {

        // Material3 dropdown wrapper
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded } // kattintásra nyit/zár
        ) {

            // ez a "mező", amire kattintva lenyílik a lista
            OutlinedTextField(
                value = selectedCategory, // itt jelenik meg a kiválasztott kategória
                onValueChange = {},
                readOnly = true, // nem írható, csak választani lehet
                label = { Text("Szűrés kategóriára") },

                // bal oldali ikon kategória alapján
                leadingIcon = {
                    Icon(
                        imageVector = categoryIconFor(selectedCategory),
                        contentDescription = null
                    )
                },

                // jobb oldali lenyíló ikon
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },

                modifier = Modifier
                    .menuAnchor() // szükséges a dropdown helyes pozíciójához
                    .fillMaxWidth()
                    .padding(12.dp)
            )

            // lenyíló lista
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false } // kattintáson kívül bezár
            ) {

                // minden kategóriára egy menüpont
                filterOptions.forEach { cat ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {

                                // kategória ikon
                                Icon(
                                    imageVector = categoryIconFor(cat),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )

                                Spacer(Modifier.width(8.dp))

                                // kategória neve
                                Text(cat)
                            }
                        },
                        onClick = {
                            onCategorySelected(cat) // kiválasztás visszajelzése
                            expanded = false        // dropdown bezárása
                        }
                    )
                }
            }
        }
    }
}

// kategória -> ikon mapping (UI segédfüggvény)
fun categoryIconFor(category: String) = when (category) {
    "Összes" -> Icons.Default.List
    "Vár" -> Icons.Default.AccountBalance
    "Templom" -> Icons.Default.Church
    "Kilátó" -> Icons.Default.Visibility
    "Múzeum" -> Icons.Default.Museum
    "Szobor" -> Icons.Default.Person
    else -> Icons.Default.Place
}