package com.example.clicker.ui.screens.reservationContact

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.reservation.ReservationDto
import com.example.clicker.data.reservation.ReservationRequest
import com.example.clicker.data.reservation.ReservationsRepository
import com.example.clicker.data.reservationContact.ReservationContactRepository
import com.example.clicker.data.reservationContact.ReservationContactRequest
import kotlinx.coroutines.launch

class ReservationContactViewModel(
    private val reservationContactRepository: ReservationContactRepository,
    private val reservationsRepository: ReservationsRepository
) : ViewModel() {

    private val internalState = mutableStateOf<ReservationContactUiState>(ReservationContactUiState.Loading)
    val state: State<ReservationContactUiState> = internalState

    var notesInput by mutableStateOf("")
        private set

    fun loadContactData(reservation: ReservationDto) {
        viewModelScope.launch {
            internalState.value = ReservationContactUiState.Loading
            try {
                val refreshedReservation =
                    reservationsRepository
                        .getReservationsByFestival(reservation.festivalId)
                        .firstOrNull { it.id == reservation.id }
                        ?: reservation

                val contacts = reservationContactRepository.getContactsByReservation(refreshedReservation.id)

                internalState.value = ReservationContactUiState.Success(
                    reservation = refreshedReservation,
                    contacts = contacts
                )
            } catch (e: Exception) {
                internalState.value = ReservationContactUiState.Error(
                    e.message ?: "Impossible de charger la prise de contact"
                )
            }
        }
    }

    fun onNotesChange(value: String) {
        notesInput = value
    }

    fun addContact(reservation: ReservationDto) {
        viewModelScope.launch {
            try {
                reservationContactRepository.createContact(
                    ReservationContactRequest(
                        reservationId = reservation.id,
                        contactId = null,
                        contactDate = null,
                        notes = notesInput.ifBlank { null }
                    )
                )

                notesInput = ""
                loadContactData(reservation)
            } catch (e: Exception) {
                internalState.value = ReservationContactUiState.Error(
                    e.message ?: "Impossible d'enregistrer la prise de contact"
                )
            }
        }
    }

    fun availableNextStatuses(currentStatus: Int): List<Int> {
        return when (currentStatus) {
            0 -> listOf(0, 1)
            1 -> listOf(1, 2, 3)
            2 -> listOf(2, 3, 5, 4)
            3 -> listOf(3)
            4 -> listOf(4)
            5 -> listOf(5, 6)
            6 -> listOf(6, 7)
            7 -> listOf(7)
            else -> listOf(currentStatus)
        }
    }

    fun changeStatus(reservation: ReservationDto, nextStatus: Int) {
        if (reservation.status == nextStatus) return

        viewModelScope.launch {
            try {
                reservationsRepository.updateReservation(
                    id = reservation.id,
                    request = ReservationRequest(
                        id = reservation.id,
                        festivalId = reservation.festivalId,
                        reservantId = reservation.reservantId,
                        status = nextStatus,
                        priceBeforeDiscount = reservation.priceBeforeDiscount,
                        discountAmount = reservation.discountAmount,
                        totalPrice = reservation.totalPrice,
                        freeTables = reservation.freeTables,
                        presentsGames = reservation.presentsGames,
                        gamesListRequested = reservation.gamesListRequested,
                        gamesListReceived = reservation.gamesListReceived,
                        gamesReceived = reservation.gamesReceived
                    )
                )

                loadContactData(reservation)
            } catch (e: Exception) {
                internalState.value = ReservationContactUiState.Error(
                    e.message ?: "Impossible de changer le statut"
                )
            }
        }
    }
}