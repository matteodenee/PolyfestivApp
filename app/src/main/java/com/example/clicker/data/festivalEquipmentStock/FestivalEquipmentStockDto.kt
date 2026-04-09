package com.example.clicker.data.festivalEquipmentStock

import kotlinx.serialization.Serializable

@Serializable
data class FestivalEquipmentStockDto(
    val id: Int,
    val festivalId: Int,
    val equipmentId: Int,
    val quantityAvailable: Int
)