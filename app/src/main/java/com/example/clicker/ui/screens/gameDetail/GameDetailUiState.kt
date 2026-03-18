package com.example.clicker.ui.screens.gameDetail

import com.example.clicker.data.game.GameDto

sealed interface GameDetailUiState {
    data object Loading : GameDetailUiState
    data class Success(val game: GameDto) : GameDetailUiState
    data class Error(val message: String) : GameDetailUiState
}
