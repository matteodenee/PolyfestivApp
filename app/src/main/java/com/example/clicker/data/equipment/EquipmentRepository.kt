package com.example.clicker.data.equipment

class EquipmentRepository(
    private val api: EquipmentApiService
) {
    suspend fun getEquipmentsByFestival(festivalId: Int): List<EquipmentDto> {
        return api.getEquipments(festivalId = festivalId)
    }
}