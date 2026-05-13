package hu.bme.aut.android.publictransport.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.bme.aut.android.publictransport.R

@Composable
fun LoginScreen(
    onSuccess: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        //TODO Logo
        //Logo
        Image(
            painter = painterResource(id = R.mipmap.ic_transport_round),
            contentDescription = "Logo",
            modifier = Modifier.size(160.dp)
        )

        //TODO Header Text
        //Header Text
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Please enter your credentials!"
        )

        //TODO Email Field
        //Email Field
        var email by remember { mutableStateOf("") }
        var emailError by remember { mutableStateOf(false) }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Email") },
            value = email,
            onValueChange =
                {
                    email = it
                    emailError = isEmailValid(email)
                },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            isError = emailError,
            trailingIcon = {
                if (emailError) {
                    Icon(Icons.Filled.Warning, contentDescription = "Error", tint = Color.Red)
                }
            },
            supportingText = {
                if (emailError) {
                    Text("Please enter your e-mail address!", color = Color.Red)
                }
            }

        )

        //TODO Password Field
        //Password Field
        var password by remember { mutableStateOf("") }
        var passwordError by remember { mutableStateOf(false) }

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            label = { Text("Password") },
            value = password,
            onValueChange =
                {
                    password = it
                    passwordError = isPasswordValid(it)
                },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = passwordError,
            trailingIcon = {
                if (passwordError) {
                    Icon(Icons.Filled.Warning, contentDescription = "Error", tint = Color.Red)
                }
            },
            supportingText = {
                if (passwordError) {
                    Text("Please enter your password!", color = Color.Red)
                }
            }
        )

        //TODO Login Button
        //Login Button
        Button(
            onClick = {
                if (isEmailValid(email)) {
                    emailError = true
                } else if (isPasswordValid(password)) {
                    passwordError = true
                } else {
                    onSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text("Login")
        }
    }
}


private fun isEmailValid(email: String) = email.isEmpty()

private fun isPasswordValid(password: String) = password.isEmpty()

@Preview
@Composable
fun PreviewLoginScreen() {
    LoginScreen(onSuccess = {})
}
