package com.example.clicker.ui.screens.reservationCreate

sealed interface ReservationCreateUiState {
    data object Loading : ReservationCreateUiState
    data object Success : ReservationCreateUiState
    data class Error(val message: String) : ReservationCreateUiState
}