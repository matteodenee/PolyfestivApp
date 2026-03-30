package com.example.clicker.data.tarifZone

import retrofit2.http.GET
import retrofit2.http.Query

interface TarifZoneApiService {

    @GET("api/tariffzones")
    suspend fun getTarifZones(
        @Query("festivalId") festivalId: Int? = null
    ): List<TarifZoneDto>
}