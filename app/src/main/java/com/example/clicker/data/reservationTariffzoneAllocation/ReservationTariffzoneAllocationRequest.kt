package com.example.clicker.data.reservationTariffzoneAllocation

import kotlinx.serialization.Serializable

@Serializable
data class ReservationTariffzoneAllocationRequest(
    val reservationId: Int,
    val tariffzoneId: Int,
    val quantityTables: Int? = null,
    val quantityAreaSqm: Int? = null
)