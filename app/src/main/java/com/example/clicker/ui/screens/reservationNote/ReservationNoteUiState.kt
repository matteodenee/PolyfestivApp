package com.example.clicker.ui.screens.reservationNote

import com.example.clicker.data.reservationNote.ReservationNoteDto

sealed interface ReservationNoteUiState {
    data object Loading : ReservationNoteUiState
    data class Success(val notes: List<ReservationNoteDto>) : ReservationNoteUiState
    data class Error(val message: String) : ReservationNoteUiState
}