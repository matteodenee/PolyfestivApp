package com.example.clicker.ui.screens.reservations

import com.example.clicker.data.reservation.ReservationDto

sealed interface ReservationsUiState {
    data object Loading : ReservationsUiState
    data class Success(val reservations: List<ReservationDto>) : ReservationsUiState
    data class Error(val message: String) : ReservationsUiState
}