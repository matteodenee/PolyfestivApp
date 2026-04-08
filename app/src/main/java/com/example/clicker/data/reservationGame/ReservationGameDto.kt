package com.example.clicker.data.reservationGame

import kotlinx.serialization.Serializable

@Serializable
data class ReservationGameDto(
    val id: Int,
    val reservationId: Int,
    val gameId: Int,
    val editorActorId: Int? = null,
    val tablesNeeded: Int? = null,
    val chairsNeeded: Int? = null,
    val outletsNeeded: Int? = null
)