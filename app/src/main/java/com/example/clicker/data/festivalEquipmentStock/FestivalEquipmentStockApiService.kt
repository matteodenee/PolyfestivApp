package com.example.clicker.data.festivalEquipmentStock

import retrofit2.http.GET
import retrofit2.http.Query

interface FestivalEquipmentStockApiService {

    @GET("api/festival-equipment-stocks")
    suspend fun getStocks(
        @Query("festivalId") festivalId: Int? = null
    ): List<FestivalEquipmentStockDto>
}