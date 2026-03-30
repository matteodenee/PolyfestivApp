package com.example.clicker.data.exposants

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorFestivalLinkDto(
    val id: Int,
    @SerialName("festival_id") val festivalId: Int,
    @SerialName("actor_id") val actorId: Int,
    val contacted: Boolean? = null,
    @SerialName("last_contact_date") val lastContactDate: String? = null,
    val status: String? = null,
    @SerialName("has_reservation") val hasReservation: Boolean? = null,
)