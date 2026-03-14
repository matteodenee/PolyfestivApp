package com.example.clicker.ui.screens.register


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.ui.AppViewModelProvider
import com.example.clicker.ui.theme.ButtonBlue
import com.example.clicker.ui.theme.SearchField

@Composable
fun RegisterScreen(
    onRegisterSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
    registerViewModel: RegisterViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state = registerViewModel.state.value
    val login by registerViewModel.loginText
    val password by registerViewModel.passwordText

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (val uiState = state) {
            is RegisterUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is RegisterUiState.Success -> {
                LaunchedEffect(Unit) {
                    onRegisterSuccess()
                }
            }

            else -> {
                Text(
                    text = "Inscription",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = login,
                    onValueChange = registerViewModel::onLoginChange,
                    label = { Text("login") },
                    singleLine = true,
                    shape = RoundedCornerShape(6.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SearchField,
                        unfocusedContainerColor = SearchField,
                        disabledContainerColor = SearchField,
                        focusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.fillMaxWidth(0.65f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = registerViewModel::onPasswordChange,
                    label = { Text("password") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(6.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = SearchField,
                        unfocusedContainerColor = SearchField,
                        disabledContainerColor = SearchField,
                        focusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
                        unfocusedBorderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    ),
                    modifier = Modifier.fillMaxWidth(0.65f)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = { registerViewModel.register() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonBlue,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    ),
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Text("Valider")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Vous avez déjà un compte ?",
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onNavigateToLogin,
                    shape = RoundedCornerShape(22.dp)
                ) {
                    Text("Connexion")
                }

                if (uiState is RegisterUiState.Error) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = uiState.message,
                        color = Color.Red
                    )
                }
            }
        }
    }
}