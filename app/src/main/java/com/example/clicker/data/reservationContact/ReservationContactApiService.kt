package com.example.clicker.data.reservationContact

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ReservationContactApiService {

    @GET("api/reservation-contacts")
    suspend fun getContacts(
        @Query("reservationId") reservationId: Int? = null,
        @Query("festivalId") festivalId: Int? = null
    ): List<ReservationContactDto>

    @POST("api/reservation-contacts")
    suspend fun createContact(
        @Body request: ReservationContactRequest
    ): ReservationContactDto
}