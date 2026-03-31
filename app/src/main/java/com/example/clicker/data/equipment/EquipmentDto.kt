package com.example.clicker.data.equipment

import kotlinx.serialization.Serializable

@Serializable
data class EquipmentDto(
    val id: Int,
    val festivalId: Int,
    val kind: String,
    val unitPrice: Double,
    val quantity: Int
)