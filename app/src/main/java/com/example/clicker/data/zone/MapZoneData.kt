package com.example.clicker.data.zone

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
data class MapZoneDto(
    val id: Int,
    val name: String,
    val festivalId: Int,
    val nbtable: Int,
    val surface: Double,
    val tariffzoneid: Int,
    val description: String
)

@Serializable
data class MapZoneRequest(
    val name: String,
    val festivalId: Int,
    val nbtable: Int,
    val surface: Double,
    val tariffzoneid: Int,
    val description: String
)

interface MapZonesApiService {
    @GET("api/zonemaps")
    suspend fun getMapZonesByFestival(@Query("festivalId") festivalId: Int): List<MapZoneDto>

    @POST("api/zonemaps")
    suspend fun createMapZone(@Body request: MapZoneRequest): MapZoneDto

    @POST("api/zonemaps/{id}")
    suspend fun updateMapZone(@Path("id") id: Int, @Body request: MapZoneDto): MapZoneDto

    @DELETE("api/zonemaps/{id}")
    suspend fun deleteMapZone(@Path("id") id: Int)
}

class MapZonesRepository(private val api: MapZonesApiService) {
    suspend fun getMapZonesByFestival(festivalId: Int): List<MapZoneDto> = api.getMapZonesByFestival(festivalId)
    suspend fun createMapZone(request: MapZoneRequest): MapZoneDto = api.createMapZone(request)
    suspend fun updateMapZone(id: Int, dto: MapZoneDto): MapZoneDto = api.updateMapZone(id, dto)
    suspend fun deleteMapZone(id: Int) = api.deleteMapZone(id)
}
