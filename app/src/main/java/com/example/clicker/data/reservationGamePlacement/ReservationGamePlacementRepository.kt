package com.example.clicker.data.reservationGamePlacement

class ReservationGamePlacementRepository(
    private val api: ReservationGamePlacementApiService
) {
    suspend fun getPlacementsByFestival(festivalId: Int): List<ReservationGamePlacementDto> {
        return api.getPlacements(festivalId = festivalId)
    }

    suspend fun createPlacement(
        request: ReservationGamePlacementRequest
    ): ReservationGamePlacementDto {
        return api.createPlacement(request)
    }

    suspend fun deletePlacement(id: Int) {
        api.deletePlacement(id)
    }
}