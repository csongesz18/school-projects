package hu.bme.aut.android.androidwallet.ui.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title: String, sum: Int?, icon: ImageVector, onIconClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = title) },
        actions = {
            if (sum != null) {
                Text(
                    text = "Sum: $sum Ft",
                    modifier = Modifier.padding(end = 8.dp),
                    color = Color.Black
                )
            }
            IconButton(onClick = onIconClick) {
                Icon(imageVector = icon, contentDescription = "Delete", tint = Color.Red)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.inversePrimary)
    )
}

/* J69X80 */

@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun PreviewTopBar() {
    val salaryItems = null
    TopBar(title = "AndroidWallet", sum = null, icon = Icons.Default.Clear) {

    }
}
