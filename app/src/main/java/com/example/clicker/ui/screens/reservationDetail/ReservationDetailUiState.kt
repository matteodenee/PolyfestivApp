package com.example.clicker.ui.screens.reservationDetail

sealed interface ReservationDetailUiState {
    data object Idle : ReservationDetailUiState
    data object Loading : ReservationDetailUiState
    data object Success : ReservationDetailUiState
    data class Error(val message: String) : ReservationDetailUiState
}