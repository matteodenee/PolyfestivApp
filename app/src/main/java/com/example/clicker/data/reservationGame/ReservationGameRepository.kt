package com.example.clicker.data.reservationGame

class ReservationGameRepository(
    private val api: ReservationGameApiService
) {
    suspend fun getGamesByReservation(reservationId: Int): List<ReservationGameDto> {
        return api.getReservationGames(reservationId = reservationId)
    }

    suspend fun createReservationGame(
        request: ReservationGameRequest
    ): ReservationGameDto {
        return api.createReservationGame(request)
    }

    suspend fun deleteReservationGame(id: Int) {
        api.deleteReservationGame(id)
    }
}