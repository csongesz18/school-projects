package hu.magyarvandor.app.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import hu.magyarvandor.app.ui.categoryIcon // ✅ központi ikon használat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlaceDetailsScreen(
    onBack: () -> Unit,
    onOpenMapToPlace: (lat: Double, lon: Double, name: String) -> Unit,
    viewModel: PlaceDetailsViewModel = hiltViewModel()
) {
    val place by viewModel.place.collectAsState(initial = null)
    val images by viewModel.images.collectAsState(initial = emptyList())
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(place?.name ?: "Részletek") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Vissza")
                    }
                }
            )
        }
    ) { innerPadding ->

        if (place == null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Nincs ilyen hely")
            }
            return@Scaffold
        }

        val p = place!!

        // Ha van több kép → galéria, különben fallback imageUri
        val gallery: List<String> =
            if (images.isNotEmpty()) images
            else p.imageUri?.takeIf { it.isNotBlank() }?.let { listOf(it) } ?: emptyList()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
        ) {

            // ===== KÉPGALÉRIA =====
            if (gallery.isNotEmpty()) {
                val pagerState = rememberPagerState(pageCount = { gallery.size })

                Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.fillMaxSize()
                    ) { page ->
                        AsyncImage(
                            model = gallery[page],
                            contentDescription = p.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // alsó sötét overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.65f))
                                )
                            )
                    )

                    // kategória chip
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Row {
                            CategoryChip(category = p.category)
                        }
                    }

                    // pötty indikátor
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeat(gallery.size) { i ->
                            val selected = i == pagerState.currentPage
                            Box(
                                modifier = Modifier
                                    .size(if (selected) 8.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (selected) Color.White
                                        else Color.White.copy(alpha = 0.45f)
                                    )
                            )
                        }
                    }
                }
            }

            // ===== TARTALOM =====
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                // térkép gomb
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Helyszín")
                        Spacer(Modifier.height(8.dp))

                        Text("Szélesség: ${p.latitude}\nHosszúság: ${p.longitude}")

                        Spacer(Modifier.height(12.dp))

                        Button(
                            onClick = {
                                // saját térképre navigál
                                onOpenMapToPlace(p.latitude, p.longitude, p.name)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.Map, contentDescription = null)
                            Spacer(Modifier.size(8.dp))
                            Text("Megnyitás térképen")
                        }
                    }
                }

                ExpandableCard("Leírás", p.description)
                ExpandableCard("Történelmi leírás", p.history)

                HorizontalDivider()

                Text(
                    text = "MagyarVándor",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

// ===== KATEGÓRIA CHIP =====
@Composable
private fun CategoryChip(category: String) {

    // KÖZPONTI IKON használata (nincs duplikáció)
    val icon = categoryIcon(category)

    AssistChip(
        onClick = { },
        label = { Text(category) },
        leadingIcon = {
            Icon(icon, contentDescription = null, modifier = Modifier.size(18.dp))
        },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = Color.White.copy(alpha = 0.18f),
            labelColor = Color.White,
            leadingIconContentColor = Color.White
        )
    )
}

// ===== LENYITHATÓ SZÖVEG =====
@Composable
private fun ExpandableCard(
    title: String,
    text: String
) {
    var expanded by remember { mutableStateOf(false)}

    Card {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title)
            Spacer(Modifier.height(8.dp))

            Text(
                text = text,
                maxLines = if (expanded) Int.MAX_VALUE else 4,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 22.sp
                )
            )

            if (text.length > 160) {
                Spacer(Modifier.height(10.dp))
                AssistChip(
                    onClick = { expanded = !expanded },
                    label = { Text(if (expanded) "Kevesebb" else "Tovább") }
                )
            }
        }
    }
}