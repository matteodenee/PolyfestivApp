package com.example.clicker.ui.screens.gameCreate

sealed interface GameCreateUiState {
    data object Idle : GameCreateUiState
    data object Loading : GameCreateUiState
    data object Success : GameCreateUiState
    data class Error(val message: String) : GameCreateUiState
}
