package com.example.clicker.data.reservationTariffzoneAllocation

class ReservationTariffzoneAllocationRepository(
    private val api: ReservationTariffzoneAllocationApiService
) {
    suspend fun getAllocationsByReservation(reservationId: Int): List<ReservationTariffzoneAllocationDto> {
        return api.getAllocations(reservationId = reservationId)
    }

    suspend fun createAllocation(
        request: ReservationTariffzoneAllocationRequest
    ): ReservationTariffzoneAllocationDto {
        return api.createAllocation(request)
    }

    suspend fun deleteAllocation(id: Int) {
        api.deleteAllocation(id)
    }

    suspend fun updateAllocation(
        id: Int,
        request: ReservationTariffzoneAllocationRequest
    ): ReservationTariffzoneAllocationDto {
        return api.updateAllocation(id, request)
    }
}