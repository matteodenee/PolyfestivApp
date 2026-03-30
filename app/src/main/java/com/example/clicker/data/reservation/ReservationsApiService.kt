package com.example.clicker.data.reservation

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReservationsApiService {

    @GET("api/reservations")
    suspend fun getReservations(
        @Query("festivalId") festivalId: Int? = null,
        @Query("reservantId") reservantId: Int? = null
    ): List<ReservationDto>

    @POST("api/reservations")
    suspend fun createReservation(
        @Body request: ReservationRequest
    ): ReservationDto

    @POST("api/reservations/{id}")
    suspend fun updateReservation(
        @Path("id") id: Int,
        @Body request: ReservationRequest
    ): ReservationDto

    @DELETE("api/reservations/{id}")
    suspend fun deleteReservation(
        @Path("id") id: Int
    ): Response<Unit>
}