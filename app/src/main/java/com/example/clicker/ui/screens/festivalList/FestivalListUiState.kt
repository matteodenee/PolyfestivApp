package com.example.clicker.ui.screens.festivalList

import com.example.clicker.data.festival.FestivalDto

sealed interface FestivalListUiState {
    data object Loading : FestivalListUiState
    data class Success(val festivals: List<FestivalDto>) : FestivalListUiState
    data class Error(val message: String) : FestivalListUiState
}
