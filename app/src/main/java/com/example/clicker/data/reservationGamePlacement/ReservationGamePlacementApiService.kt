package com.example.clicker.data.reservationGamePlacement

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReservationGamePlacementApiService {

    @GET("api/reservation-game-placements")
    suspend fun getPlacements(
        @Query("reservationId") reservationId: Int? = null,
        @Query("festivalId") festivalId: Int? = null
    ): List<ReservationGamePlacementDto>

    @POST("api/reservation-game-placements")
    suspend fun createPlacement(
        @Body request: ReservationGamePlacementRequest
    ): ReservationGamePlacementDto

    @POST("api/reservation-game-placements/{id}")
    suspend fun updatePlacement(
        @Path("id") id: Int,
        @Body request: ReservationGamePlacementRequest
    ): ReservationGamePlacementDto

    @DELETE("api/reservation-game-placements/{id}")
    suspend fun deletePlacement(
        @Path("id") id: Int
    )
}