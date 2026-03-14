package com.example.clicker.ui.screens.login

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.auth.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val loginTextState = mutableStateOf("")
    val loginText: State<String> = loginTextState

    private val passwordTextState = mutableStateOf("")
    val passwordText: State<String> = passwordTextState

    private val internalState = mutableStateOf<LoginUiState>(LoginUiState.Idle)
    val state: State<LoginUiState> = internalState

    fun onLoginChange(value: String) {
        loginTextState.value = value
    }

    fun onPasswordChange(value: String) {
        passwordTextState.value = value
    }

    fun login(onSuccessNavigate: () -> Unit) {
        val login = loginTextState.value.trim()
        val password = passwordTextState.value

        if (login.isBlank() || password.isBlank()) {
            internalState.value = LoginUiState.Error("Remplis le login et le mot de passe")
            Log.d(TAG, "Login refusé : champs vides")
            return
        }

        viewModelScope.launch {
            internalState.value = LoginUiState.Loading
            try {
                val response = authRepository.login(login, password)
                Log.d(TAG, "Connexion réussie pour : ${response.user.login}")
                internalState.value = LoginUiState.Success(response.user)
            } catch (e: Exception) {
                Log.e(TAG, "Erreur de connexion", e)
                internalState.value =
                    LoginUiState.Error("Échec de connexion " + (e.message ?: ""))
            }
        }
    }
}