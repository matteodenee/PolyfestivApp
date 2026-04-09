package com.example.clicker.data.festivalEquipmentStock

class FestivalEquipmentStockRepository(
    private val api: FestivalEquipmentStockApiService
) {
    suspend fun getStocksByFestival(festivalId: Int): List<FestivalEquipmentStockDto> {
        return api.getStocks(festivalId = festivalId)
    }
}