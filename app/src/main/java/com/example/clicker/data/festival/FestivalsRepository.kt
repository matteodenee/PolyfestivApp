package com.example.clicker.data.festival

class FestivalsRepository(private val api: FestivalsApiService) {
    suspend fun getFestivals(): List<FestivalDto> = api.getFestivals()
    suspend fun getFestivalById(id: Int): FestivalDto = api.getFestivalById(id)
    suspend fun createFestival(request: FestivalRequest): FestivalDto = api.createFestival(request)
    suspend fun updateFestival(id: Int, request: FestivalRequest): FestivalDto = api.updateFestival(id, request)
    suspend fun deleteFestival(id: Int) = api.deleteFestival(id)
}
