package com.example.clicker.ui.screens.gameEdit

sealed interface GameEditUiState {
    data object Loading : GameEditUiState
    data object Ready : GameEditUiState
    data object Saving : GameEditUiState
    data object Success : GameEditUiState
    data class Error(val message: String) : GameEditUiState
}
