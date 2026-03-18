package com.example.clicker.data.festival

import com.example.clicker.data.game.GameDto
import retrofit2.http.GET
import retrofit2.http.Path

interface FestivalsApiService {
    @GET("api/festivals")
    suspend fun getFestivals(): List<FestivalDto>

    @GET("api/festivals/{id}")
    suspend fun getFestivalById(@Path("id") id: Int): FestivalDto


}
