package com.example.clicker.ui.screens.reservationDetail

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.data.exposants.typeLabel
import com.example.clicker.data.reservation.ReservationsRepository
import kotlinx.coroutines.launch

class ReservationDetailViewModel(
    private val reservationsRepository: ReservationsRepository,
    private val exposantRepository: ExposantRepository
) : ViewModel() {

    private val internalState = mutableStateOf<ReservationDetailUiState>(ReservationDetailUiState.Idle)
    val state: State<ReservationDetailUiState> = internalState

    private val _reservantName = mutableStateOf("")
    val reservantName: State<String> = _reservantName

    private val _reservantType = mutableStateOf("")
    val reservantType: State<String> = _reservantType

    fun loadReservantInfo(reservantId: Int) {
        viewModelScope.launch {
            try {
                val exposants = exposantRepository.getExposants()
                val exposant = exposants.find { it.id == reservantId }

                _reservantName.value = exposant?.name ?: "Acteur $reservantId"
                _reservantType.value = exposant?.typeLabel() ?: "Inconnu"
            } catch (_: Exception) {
                _reservantName.value = "Acteur $reservantId"
                _reservantType.value = "Inconnu"
            }
        }
    }

    fun deleteReservation(reservationId: Int, onDeleted: () -> Unit) {
        viewModelScope.launch {
            internalState.value = ReservationDetailUiState.Loading
            try {
                reservationsRepository.deleteReservation(reservationId)
                onDeleted()
            } catch (e: Exception) {
                internalState.value = ReservationDetailUiState.Error(
                    e.message ?: "Erreur lors de la suppression"
                )
            }
        }
    }
}