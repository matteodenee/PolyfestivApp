package com.example.clicker.ui.screens.reservationContact

import com.example.clicker.data.reservation.ReservationDto
import com.example.clicker.data.reservationContact.ReservationContactDto

sealed interface ReservationContactUiState {
    data object Loading : ReservationContactUiState

    data class Success(
        val reservation: ReservationDto,
        val contacts: List<ReservationContactDto>
    ) : ReservationContactUiState

    data class Error(val message: String) : ReservationContactUiState
}