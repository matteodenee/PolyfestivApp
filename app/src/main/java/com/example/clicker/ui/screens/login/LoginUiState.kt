package com.example.clicker.ui.screens.login

import com.example.clicker.data.auth.UserDto

sealed class LoginUiState {
    object Idle : LoginUiState() // Etat initial (l'écran est pret)
    object Loading : LoginUiState()
    data class Success(val user: UserDto) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}