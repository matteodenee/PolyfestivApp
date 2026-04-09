package com.example.clicker.data.reservationGamePlacement

import kotlinx.serialization.Serializable

@Serializable
data class ReservationGamePlacementRequest(
    val reservationId: Int,
    val gameId: Int,
    val tablesAllocated: Int = 0,
    val tableType: String,
    val chairsAllocated: Int? = null,
    val outletsAllocated: Int? = null,
    val mapzoneId: Int? = null
)