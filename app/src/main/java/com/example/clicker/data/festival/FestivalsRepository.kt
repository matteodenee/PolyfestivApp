package com.example.clicker.data.festival

class FestivalsRepository(private val api: FestivalsApiService) {
    suspend fun getFestivals(): List<FestivalDto> = api.getFestivals()
    suspend fun getFestivalById(id: Int): FestivalDto = api.getFestivalById(id)
}
