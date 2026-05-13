package hu.magyarvandor.app.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import hu.magyarvandor.app.R

@Composable
fun AdminPanelScreen(
    onAddPlace: () -> Unit,      // új hely hozzáadása navigáció
    onEditPlaces: () -> Unit,    // helyek szerkesztése navigáció
    onBack: () -> Unit           // visszalépés
) {
    // Scaffold alap layout (Material design struktúra)
    Scaffold(
        contentWindowInsets = WindowInsets(0) // teljes képernyő kihasználása
    ) { padding ->

        // háttér + középre igazítás
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {

            // fő oszlop (középre rendezett tartalom)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ===== LOGÓ =====
                Image(
                    painter = painterResource(id = R.drawable.logo_mv),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(200.dp) // fix méret
                        .padding(bottom = 8.dp)
                )

                // cím
                Text(
                    text = "Admin panel",
                    style = MaterialTheme.typography.headlineSmall
                )

                // alcím
                Text(
                    text = "Kezeld az alkalmazás tartalmát",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // ===== KÁRTYA (gombok csoportja) =====
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp)) // lekerekítés
                        .background(MaterialTheme.colorScheme.surface) // kártya háttér
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // térköz a gombok között
                ) {

                    // új hely hozzáadása gomb
                    Button(
                        onClick = onAddPlace,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("➕  Új hely hozzáadása")
                    }

                    // helyek szerkesztése gomb
                    Button(
                        onClick = onEditPlaces,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("✏️  Helyek szerkesztése")
                    }

                    // vissza gomb (secondary action)
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("⬅  Vissza")
                    }
                }
            }
        }
    }
}