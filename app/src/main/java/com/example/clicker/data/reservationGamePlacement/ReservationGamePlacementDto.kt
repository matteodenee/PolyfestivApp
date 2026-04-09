package com.example.clicker.data.reservationGamePlacement

import kotlinx.serialization.Serializable

@Serializable
data class ReservationGamePlacementDto(
    val id: Int,
    val reservationId: Int,
    val gameId: Int,
    val tablesAllocated: Int,
    val tableType: String,
    val chairsAllocated: Int? = null,
    val outletsAllocated: Int? = null,
    val mapzoneId: Int? = null
)