package com.example.clicker.ui.screens.register

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.auth.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import com.example.clicker.TAG

class RegisterViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val loginTextState = mutableStateOf("")
    val loginText: State<String> = loginTextState

    private val passwordTextState = mutableStateOf("")
    val passwordText: State<String> = passwordTextState

    private val internalState = mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
    val state: State<RegisterUiState> = internalState

    fun onLoginChange(value: String) {
        loginTextState.value = value
    }

    fun onPasswordChange(value: String) {
        passwordTextState.value = value
    }

    fun register() {
        val login = loginTextState.value.trim()
        val password = passwordTextState.value

        if (login.isBlank() || password.isBlank()) {
            internalState.value = RegisterUiState.Error("Remplis le login et le mot de passe")
            Log.d(TAG, "Register refusé : champs vides")
            return
        }

        viewModelScope.launch {
            internalState.value = RegisterUiState.Loading

            try {
                val response = authRepository.register(login, password)
                Log.d(TAG, "Inscription réussie pour : ${response.user.login}")
                internalState.value = RegisterUiState.Success
            } catch (e: HttpException) {
                Log.e(TAG, "Erreur HTTP lors de l'inscription : ${e.code()}", e)
                internalState.value = when (e.code()) {
                    409 -> RegisterUiState.Error("Ce login est déjà utilisé")
                    400 -> RegisterUiState.Error("Champs manquants")
                    else -> RegisterUiState.Error("Erreur serveur (${e.code()})")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Serveur injoignable", e)
                internalState.value = RegisterUiState.Error("Serveur injoignable")
            } catch (e: Exception) {
                internalState.value = RegisterUiState.Error("Erreur inconnue")
            }
        }
    }
}