package com.example.clicker.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActorDto(
    val id: Int,
    val name: String,
    val type: List<String> = emptyList(),
    val description: String? = null,
    val email: String? = null,
    val phone: String? = null
)