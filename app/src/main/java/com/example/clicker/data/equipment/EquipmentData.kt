package com.example.clicker.data.equipment

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

@Serializable
data class EquipmentDto(
    val id: Int,
    val festivalId: Int,
    val kind: String,
    val unitPrice: Double,
    val quantity: Int
)

@Serializable
data class EquipmentRequest(
    val festivalId: Int,
    val kind: String,
    val unitPrice: Double,
    val quantity: Int
)

interface EquipmentsApiService {
    @GET("api/equipments")
    suspend fun getEquipmentsByFestival(@Query("festivalId") festivalId: Int): List<EquipmentDto>

    @POST("api/equipments")
    suspend fun createEquipment(@Body request: EquipmentRequest): EquipmentDto

    @POST("api/equipments/{id}")
    suspend fun updateEquipment(@Path("id") id: Int, @Body request: EquipmentDto): EquipmentDto

    @DELETE("api/equipments/{id}")
    suspend fun deleteEquipment(@Path("id") id: Int)
}

class EquipmentsRepository(private val api: EquipmentsApiService) {
    suspend fun getEquipmentsByFestival(festivalId: Int): List<EquipmentDto> = api.getEquipmentsByFestival(festivalId)
    suspend fun createEquipment(request: EquipmentRequest): EquipmentDto = api.createEquipment(request)
    suspend fun updateEquipment(id: Int, dto: EquipmentDto): EquipmentDto = api.updateEquipment(id, dto)
    suspend fun deleteEquipment(id: Int) = api.deleteEquipment(id)
}
