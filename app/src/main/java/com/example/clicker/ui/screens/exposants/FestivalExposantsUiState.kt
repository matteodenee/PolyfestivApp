package com.example.clicker.ui.screens.exposants

import com.example.clicker.data.exposants.FestivalExposantItem

sealed interface FestivalExposantsUiState {
    data object Loading : FestivalExposantsUiState
    data class Success(val exposants: List<FestivalExposantItem>) : FestivalExposantsUiState
    data class Error(val message: String) : FestivalExposantsUiState
}