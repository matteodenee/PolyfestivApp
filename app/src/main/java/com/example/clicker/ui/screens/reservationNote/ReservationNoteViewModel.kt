package com.example.clicker.ui.screens.reservationNote

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.reservation.ReservationDto
import com.example.clicker.data.reservationNote.ReservationNoteRepository
import com.example.clicker.data.reservationNote.ReservationNoteRequest
import kotlinx.coroutines.launch

class ReservationNoteViewModel(
    private val reservationNoteRepository: ReservationNoteRepository
) : ViewModel() {

    private val internalState = mutableStateOf<ReservationNoteUiState>(ReservationNoteUiState.Loading)
    val state: State<ReservationNoteUiState> = internalState

    var noteInput by mutableStateOf("")
        private set

    fun loadNotes(reservationId: Int) {
        viewModelScope.launch {
            internalState.value = ReservationNoteUiState.Loading
            try {
                val notes = reservationNoteRepository.getNotesByReservation(reservationId)
                internalState.value = ReservationNoteUiState.Success(notes)
            } catch (e: Exception) {
                internalState.value = ReservationNoteUiState.Error(e.message ?: "Impossible de charger les notes")
            }
        }
    }

    fun onNoteChange(value: String) {
        noteInput = value
    }

    fun addNote(reservation: ReservationDto) {
        val content = noteInput.trim()
        if (content.isBlank()) return

        viewModelScope.launch {
            try {
                reservationNoteRepository.createNote(
                    ReservationNoteRequest(
                        reservationId = reservation.id,
                        content = content
                    )
                )
                noteInput = ""
                loadNotes(reservation.id)
            } catch (e: Exception) {
                internalState.value = ReservationNoteUiState.Error(e.message ?: "Impossible d'ajouter la note")
            }
        }
    }
}