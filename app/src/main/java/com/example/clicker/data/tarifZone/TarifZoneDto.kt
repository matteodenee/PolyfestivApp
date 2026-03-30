package com.example.clicker.data.tarifZone

import kotlinx.serialization.Serializable

@Serializable
data class TarifZoneDto(
    val id: Int,
    val name: String,
    val festivalId: Int,
    val nbtable: Int,
    val tableprice: Double,
    val pricem2: Double,
    val availableTables: Int
)