package com.example.clicker.data.reservation

class ReservationsRepository(
    private val api: ReservationsApiService
) {

    suspend fun getReservationsByFestival(festivalId: Int): List<ReservationDto> {
        return api.getReservations(festivalId = festivalId)
    }

    suspend fun createReservation(request: ReservationRequest): ReservationDto {
        return api.createReservation(request)
    }

    suspend fun updateReservation(id: Int, request: ReservationRequest): ReservationDto {
        return api.updateReservation(id, request)
    }

    suspend fun deleteReservation(id: Int) {
        api.deleteReservation(id)
    }
}