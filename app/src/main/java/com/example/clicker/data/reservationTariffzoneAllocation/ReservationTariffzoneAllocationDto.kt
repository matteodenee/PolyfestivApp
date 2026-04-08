package com.example.clicker.data.reservationTariffzoneAllocation

import kotlinx.serialization.Serializable

@Serializable
data class ReservationTariffzoneAllocationDto(
    val id: Int,
    val reservationId: Int,
    val tariffzoneId: Int,
    val quantityTables: Int = 0,
    val quantityAreaSqm: Int = 0
)