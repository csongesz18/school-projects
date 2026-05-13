package hu.magyarvandor.app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import hu.magyarvandor.app.R

// Admin bejelentkező képernyő
@Composable
fun AdminLoginScreen(
    onBack: () -> Unit,              // visszalépés callback
    onLoginSuccess: () -> Unit,      // sikeres login után navigáció
    viewModel: AdminLoginViewModel = hiltViewModel() // ViewModel DI-vel
) {
    // UI state figyelése (username, password, error, login állapot)
    val state by viewModel.state.collectAsState()

    // Figyeli a login állapotot, és ha sikeres → navigál
    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            onLoginSuccess()       // továbblépés
            viewModel.consumeLogin() // flag reset
        }
    }

    // Alap layout
    Scaffold(
        contentWindowInsets = WindowInsets(0)
    ) { padding ->

        // Középre igazított tartalom
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // ===== LOGO =====
                Image(
                    painter = painterResource(id = R.drawable.logo_mv),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 16.dp)
                )

                // Cím
                Text(
                    text = "Admin belépés",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // ===== KÁRTYA STÍLUSÚ FORM =====
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    // Felhasználónév mező
                    OutlinedTextField(
                        value = state.username,
                        onValueChange = viewModel::onUsernameChange,
                        label = { Text("Felhasználónév") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Jelszó mező (elrejtett karakterekkel)
                    OutlinedTextField(
                        value = state.password,
                        onValueChange = viewModel::onPasswordChange,
                        label = { Text("Jelszó") },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Hibaüzenet megjelenítése (ha van)
                    state.error?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Belépés gomb
                    Button(
                        onClick = { viewModel.login() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Belépés")
                    }

                    // Vissza gomb
                    OutlinedButton(
                        onClick = onBack,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Vissza")
                    }
                }
            }
        }
    }
}