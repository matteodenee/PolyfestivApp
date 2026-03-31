package com.example.clicker.data.equipment

import retrofit2.http.GET
import retrofit2.http.Query

interface EquipmentApiService {

    @GET("api/equipments")
    suspend fun getEquipments(
        @Query("festivalId") festivalId: Int? = null
    ): List<EquipmentDto>
}