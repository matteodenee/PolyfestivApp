package com.example.clicker.data.mapZone

import kotlinx.serialization.Serializable

@Serializable
data class MapZoneDto(
    val id: Int,
    val name: String,
    val festivalId: Int,
    val nbtable: Int,
    val surface: Double,
    val tariffzoneid: Int,
    val description: String
)