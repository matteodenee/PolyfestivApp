package com.example.clicker.data.zone

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
data class TariffZoneDto(
    val id: Int,
    val name: String,
    val festivalId: Int,
    val nbtable: Int,
    val tableprice: Double,
    val pricem2: Double,
    val availableTables: Int
)

@Serializable
data class TariffZoneRequest(
    val name: String,
    val festivalId: Int,
    val nbtable: Int,
    val tableprice: Double,
    val pricem2: Double,
    val availableTables: Int
)

interface TariffZonesApiService {
    @GET("api/tariffzones")
    suspend fun getTariffZonesByFestival(@Query("festivalId") festivalId: Int): List<TariffZoneDto>

    @POST("api/tariffzones")
    suspend fun createTariffZone(@Body request: TariffZoneRequest): TariffZoneDto

    @POST("api/tariffzones/{id}")
    suspend fun updateTariffZone(@Path("id") id: Int, @Body request: TariffZoneDto): TariffZoneDto

    @DELETE("api/tariffzones/{id}")
    suspend fun deleteTariffZone(@Path("id") id: Int)
}

class TariffZonesRepository(private val api: TariffZonesApiService) {
    suspend fun getTariffZonesByFestival(festivalId: Int): List<TariffZoneDto> = api.getTariffZonesByFestival(festivalId)
    suspend fun createTariffZone(request: TariffZoneRequest): TariffZoneDto = api.createTariffZone(request)
    suspend fun updateTariffZone(id: Int, dto: TariffZoneDto): TariffZoneDto = api.updateTariffZone(id, dto)
    suspend fun deleteTariffZone(id: Int) = api.deleteTariffZone(id)
}
