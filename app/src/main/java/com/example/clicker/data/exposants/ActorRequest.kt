package com.example.clicker.data.exposants

import kotlinx.serialization.Serializable

@Serializable
data class ActorRequest(
    val id: Int? = null,
    val name: String,
    val type: List<String>,
    val email: String? = null,
    val phone: String? = null,
    val description: String? = null,
    val reservantType: String? = null,
    val billingAddress: String? = null,
)