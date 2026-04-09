package com.example.clicker.data.festival

import kotlinx.serialization.Serializable

@Serializable
data class FestivalRequest(
    val id: Int? = null,
    val name: String,
    val creationDate: String,
    val description: String,
    val startDate: String,
    val endDate: String
)
