package com.example.clicker.data.festivalTable

import kotlinx.serialization.Serializable

@Serializable
data class FestivalTableDto(
    val id: Int,
    val festivalId: Int,
    val type: String,
    val quantity: Int
)