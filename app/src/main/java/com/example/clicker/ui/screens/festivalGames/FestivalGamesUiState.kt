package com.example.clicker.ui.screens.festivalGames

import com.example.clicker.data.game.FestivalGameItem

sealed interface FestivalGamesUiState {
    data object Loading : FestivalGamesUiState
    data class Success(val games: List<FestivalGameItem>) : FestivalGamesUiState
    data class Error(val message: String) : FestivalGamesUiState
}