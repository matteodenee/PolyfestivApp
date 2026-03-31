package com.example.clicker.data.reservationTariffzoneAllocation

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReservationTariffzoneAllocationApiService {

    @GET("api/reservation-tariffzone-allocations")
    suspend fun getAllocations(
        @Query("reservationId") reservationId: Int? = null
    ): List<ReservationTariffzoneAllocationDto>

    @POST("api/reservation-tariffzone-allocations")
    suspend fun createAllocation(
        @Body request: ReservationTariffzoneAllocationRequest
    ): ReservationTariffzoneAllocationDto

    @DELETE("api/reservation-tariffzone-allocations/{id}")
    suspend fun deleteAllocation(
        @Path("id") id: Int
    )

    @POST("api/reservation-tariffzone-allocations/{id}")
    suspend fun updateAllocation(
        @Path("id") id: Int,
        @Body request: ReservationTariffzoneAllocationRequest
    ): ReservationTariffzoneAllocationDto
}