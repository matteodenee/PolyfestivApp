package com.example.clicker.ui.screens.festivalDetail

import com.example.clicker.data.festival.FestivalDto

sealed interface FestivalDetailUiState {
    data object Loading : FestivalDetailUiState
    data class Success(val festival: FestivalDto) : FestivalDetailUiState
    data class Error(val message: String) : FestivalDetailUiState
}
