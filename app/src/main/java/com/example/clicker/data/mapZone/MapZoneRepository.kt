package com.example.clicker.data.mapZone

class MapZoneRepository(
    private val api: MapZoneApiService
) {
    suspend fun getMapZonesByFestival(festivalId: Int): List<MapZoneDto> {
        return api.getMapZones(festivalId = festivalId)
    }

    suspend fun getMapZonesByTarifZone(tarifZoneId: Int): List<MapZoneDto> {
        return api.getMapZones(tarifZoneId = tarifZoneId)
    }
}