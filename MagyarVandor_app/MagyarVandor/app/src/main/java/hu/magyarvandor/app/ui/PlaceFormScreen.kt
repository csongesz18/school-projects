package hu.magyarvandor.app.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceFormScreen(
    onBack: () -> Unit,
    viewModel: PlaceFormViewModel = hiltViewModel()
) {
    // ViewModel állapot (mezők, képek, választott mód)
    val state by viewModel.state.collectAsState()

    // Scroll + egyszerű hibaüzenet koordinátákhoz
    val scrollState = rememberScrollState()
    var errorText by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    var categoryExpanded by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    // Photo picker (telefon/emulátor) -> hozzáadás a képlistához
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            persistReadPermissionIfPossible(context, uri)
            viewModel.addPickedImage(uri.toString())
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0),
        topBar = {
            TopAppBar(
                title = { Text(if (state.name.isBlank()) "Új hely" else "Hely szerkesztése") },
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
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            HeaderSection()

            FormCard(
                state = state,
                categoryExpanded = categoryExpanded,
                onCategoryExpandedChange = { categoryExpanded = it },
                onCategorySelected = viewModel::onCategoryChange,
                onNameChange = viewModel::onNameChange,
                onLatChange = viewModel::onLatChange,
                onLonChange = viewModel::onLonChange,
                onDescriptionChange = viewModel::onDescriptionChange,
                onHistoryChange = viewModel::onHistoryChange
            )

            ImagesCard(
                state = state,
                onImageSourceChange = viewModel::onImageSourceChange,
                onPickImage = {
                    pickImageLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onImageUrlChange = viewModel::onImageUrlChange,
                onAddUrlImage = viewModel::addUrlImage,
                onRemoveImage = viewModel::removeImage
            )

            // Hibaszöveg (ha van)
            if (errorText != null) {
                Text(
                    text = errorText!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            ActionButtons(
                isSaving = state.isSaving,
                onSave = {
                    val lat = state.latText.trim().replace(",", ".").toDoubleOrNull()
                    val lon = state.lonText.trim().replace(",", ".").toDoubleOrNull()

                    if (lat == null || lon == null) {
                        errorText = "Hibás koordináta! Példa: 46.2530 és 20.1414"
                        return@ActionButtons
                    }

                    errorText = null

                    scope.launch {
                        val success = viewModel.save(lat, lon)
                        if (success) {
                            onBack()
                        } else {
                            errorText = "A mentés nem sikerült."
                        }
                    }
                },
                onBack = onBack
            )
            Spacer(Modifier.height(8.dp))
        }
    }
}