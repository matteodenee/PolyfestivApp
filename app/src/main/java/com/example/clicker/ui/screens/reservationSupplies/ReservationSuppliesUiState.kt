package com.example.clicker.ui.screens.reservationSupplies

import com.example.clicker.data.reservation.ReservationDto
import com.example.clicker.data.reservationGame.ReservationGameDto
import com.example.clicker.data.reservationTariffzoneAllocation.ReservationTariffzoneAllocationDto
import com.example.clicker.data.tarifZone.TarifZoneDto

sealed interface ReservationSuppliesUiState {
    data object Loading : ReservationSuppliesUiState

    data class Success(
        val reservation: ReservationDto,
        val tariffZones: List<TarifZoneDto>,
        val allocations: List<ReservationTariffzoneAllocationDto>,
        val reservationGames: List<ReservationGameDto>
    ) : ReservationSuppliesUiState

    data class Error(val message: String) : ReservationSuppliesUiState
}