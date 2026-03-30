package com.example.clicker.ui.screens.exposants

import com.example.clicker.data.exposants.Exposant

sealed interface ExposantUiState {
    data object Loading : ExposantUiState
    data class Success(
        val exposants: List<Exposant>,
        val isOnline: Boolean
    ) : ExposantUiState
    data class Error(val message: String) : ExposantUiState
}