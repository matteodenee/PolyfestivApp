package com.example.clicker.data.reservationNote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReservationNoteApiService {

    @GET("api/reservation-notes")
    suspend fun getNotes(
        @Query("reservationId") reservationId: Int? = null,
        @Query("festivalId") festivalId: Int? = null
    ): List<ReservationNoteDto>

    @POST("api/reservation-notes")
    suspend fun createNote(
        @Body request: ReservationNoteRequest
    ): ReservationNoteDto
}