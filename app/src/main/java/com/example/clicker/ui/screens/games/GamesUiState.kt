package com.example.clicker.ui.screens.games

import com.example.clicker.data.game.GameDto

sealed interface GamesUiState {
    data object Loading : GamesUiState
    data class Success(val games: List<GameDto>) : GamesUiState
    data class Error(val message: String) : GamesUiState
}
