package com.example.clicker.data.festivalTable

import retrofit2.http.GET
import retrofit2.http.Query

interface FestivalTableApiService {

    @GET("api/tables")
    suspend fun getTables(
        @Query("festivalId") festivalId: Int? = null
    ): List<FestivalTableDto>
}