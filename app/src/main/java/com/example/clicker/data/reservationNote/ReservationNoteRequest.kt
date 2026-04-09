package com.example.clicker.data.reservationNote

import kotlinx.serialization.Serializable

@Serializable
data class ReservationNoteRequest(
    val reservationId: Int,
    val contactId: Int? = null,
    val author: String? = null,
    val content: String,
    val createdAt: String? = null
)