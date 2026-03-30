package com.example.clicker.data.tarifZone

class TarifZoneRepository(
    private val api: TarifZoneApiService
) {
    suspend fun getTarifZonesByFestival(festivalId: Int): List<TarifZoneDto> {
        return api.getTarifZones(festivalId = festivalId)
    }
}