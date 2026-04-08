package com.example.clicker.data.festival

import kotlinx.serialization.Serializable

@Serializable
data class FestivalDto(
    val id: Int,
    val name: String,
    val creationDate: String,
    val description: String,
    val startDate: String,
    val endDate: String
)
