package com.example.clicker.data.festival

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface FestivalsApiService {
    @GET("api/festivals")
    suspend fun getFestivals(): List<FestivalDto>

    @GET("api/festivals/{id}")
    suspend fun getFestivalById(@Path("id") id: Int): FestivalDto

    @POST("api/festivals")
    suspend fun createFestival(@Body request: FestivalRequest): FestivalDto

    @PUT("api/festivals/{id}")
    suspend fun updateFestival(@Path("id") id: Int, @Body request: FestivalRequest): FestivalDto

    @DELETE("api/festivals/{id}")
    suspend fun deleteFestival(@Path("id") id: Int)
}
