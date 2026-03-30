package com.example.clicker.data.reservationGame

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReservationGameApiService {

    @GET("api/reservation-games")
    suspend fun getReservationGames(
        @Query("reservationId") reservationId: Int? = null
    ): List<ReservationGameDto>

    @POST("api/reservation-games")
    suspend fun createReservationGame(
        @Body request: ReservationGameRequest
    ): ReservationGameDto

    @DELETE("api/reservation-games/{id}")
    suspend fun deleteReservationGame(
        @Path("id") id: Int
    )
}