package hu.magyarvandor.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import hu.magyarvandor.app.domain.PlaceCategories
import hu.magyarvandor.app.ui.model.ImageSource
import hu.magyarvandor.app.ui.model.PlaceFormUiState

@Composable
fun HeaderSection() {
    // Rövid szöveg a képernyő elején
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Add meg a hely adatait",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Kategória, koordináták, leírás és képek",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormCard(
    state: PlaceFormUiState,
    categoryExpanded: Boolean,
    onCategoryExpandedChange: (Boolean) -> Unit,
    onCategorySelected: (String) -> Unit,
    onNameChange: (String) -> Unit,
    onLatChange: (String) -> Unit,
    onLonChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onHistoryChange: (String) -> Unit
) {
    // Az űrlap mezők kártyában
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Kategória választó (ikon + szöveg)
        ExposedDropdownMenuBox(
            expanded = categoryExpanded,
            onExpandedChange = { onCategoryExpandedChange(!categoryExpanded) }
        ) {
            OutlinedTextField(
                value = state.category,
                onValueChange = {},
                readOnly = true,
                label = { Text("Kategória") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { onCategoryExpandedChange(false) }
            ) {
                PlaceCategories.all.forEach { cat ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = categoryIcon(cat),
                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(cat)
                            }
                        },
                        onClick = {
                            onCategorySelected(cat)
                            onCategoryExpandedChange(false)
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = state.name,
            onValueChange = onNameChange,
            label = { Text("Hely neve") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = state.latText,
                onValueChange = onLatChange,
                label = { Text("Szélesség") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = state.lonText,
                onValueChange = onLonChange,
                label = { Text("Hosszúság") },
                singleLine = true,
                modifier = Modifier.weight(1f)
            )
        }

        OutlinedTextField(
            value = state.description,
            onValueChange = onDescriptionChange,
            label = { Text("Leírás") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        OutlinedTextField(
            value = state.history,
            onValueChange = onHistoryChange,
            label = { Text("Történelmi leírás") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )
    }
}

@Composable
fun ImagesCard(
    state: PlaceFormUiState,
    onImageSourceChange: (ImageSource) -> Unit,
    onPickImage: () -> Unit,
    onImageUrlChange: (String) -> Unit,
    onAddUrlImage: () -> Unit,
    onRemoveImage: (String) -> Unit
) {
    // Képek hozzáadása / listázása / törlése
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Képek", style = MaterialTheme.typography.titleMedium)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = state.imageSource == ImageSource.PICKER,
                onClick = { onImageSourceChange(ImageSource.PICKER) },
                label = { Text("Telefon") },
                leadingIcon = { Icon(Icons.Filled.Image, contentDescription = null) }
            )
            FilterChip(
                selected = state.imageSource == ImageSource.URL,
                onClick = { onImageSourceChange(ImageSource.URL) },
                label = { Text("URL") },
                leadingIcon = { Icon(Icons.Filled.Link, contentDescription = null) }
            )
        }

        if (state.imageSource == ImageSource.PICKER) {
            Button(
                onClick = onPickImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Kép hozzáadása (picker)")
            }
        } else {
            OutlinedTextField(
                value = state.imageUrl,
                onValueChange = onImageUrlChange,
                label = { Text("Kép URL (https://...)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = onAddUrlImage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Kép hozzáadása (URL)")
            }
        }

        if (state.images.isNotEmpty()) {
            Divider()

            Text(
                text = "Hozzáadott képek: ${state.images.size}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                state.images.forEach { img ->
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.elevatedCardColors()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = img,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            IconButton(onClick = { onRemoveImage(img) }) {
                                Icon(Icons.Filled.Delete, contentDescription = "Törlés")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ActionButtons(
    isSaving: Boolean,
    onSave: () -> Unit,
    onBack: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Button(
            onClick = onSave,
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            if (isSaving) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            } else {
                Text("Mentés")
            }
        }

        OutlinedButton(
            onClick = onBack,
            enabled = !isSaving,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Vissza")
        }

        if (isSaving) {
            Text(
                text = "Mentés folyamatban...",
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
    }
}