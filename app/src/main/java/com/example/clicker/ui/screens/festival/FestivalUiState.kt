package com.example.clicker.ui.screens.festival

import com.example.clicker.data.festival.FestivalDto

sealed interface FestivalUiState {
    data object Loading : FestivalUiState
    data class Success(val festivals: List<FestivalDto>) : FestivalUiState
    data class Error(val message: String) : FestivalUiState
}
