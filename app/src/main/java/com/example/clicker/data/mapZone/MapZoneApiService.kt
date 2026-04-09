package com.example.clicker.data.mapZone

import retrofit2.http.GET
import retrofit2.http.Query

interface MapZoneApiService {

    @GET("api/zonemaps")
    suspend fun getMapZones(
        @Query("festivalId") festivalId: Int? = null,
        @Query("tarifZoneId") tarifZoneId: Int? = null
    ): List<MapZoneDto>
}