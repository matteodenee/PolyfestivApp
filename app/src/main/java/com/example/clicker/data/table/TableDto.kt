package com.example.clicker.data.table

import kotlinx.serialization.Serializable

@Serializable
data class TableDto(
    val id: Int,
    val festivalId: Int,
    val type: String,
    val quantity: Int
)

@Serializable
data class TableRequest(
    val festivalId: Int,
    val type: String,
    val quantity: Int
)
