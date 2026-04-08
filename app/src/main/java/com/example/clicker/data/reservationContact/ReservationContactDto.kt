package com.example.clicker.data.reservationContact

import kotlinx.serialization.Serializable

@Serializable
data class ReservationContactDto(
    val id: Int,
    val reservationId: Int,
    val contactId: Int? = null,
    val contactDate: String? = null,
    val notes: String? = null
)