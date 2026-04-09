package com.example.clicker.data.festivalTable

class FestivalTableRepository(
    private val api: FestivalTableApiService
) {
    suspend fun getTablesByFestival(festivalId: Int): List<FestivalTableDto> {
        return api.getTables(festivalId = festivalId)
    }
}