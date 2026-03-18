package com.example.clicker.data.remote.dto

import com.example.clicker.data.model.Exposant
import kotlinx.serialization.SerialName
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

fun Exposant.toActorRequest(): ActorRequest {
    return ActorRequest(
        id = if (id == 0) null else id,
        name = name,
        type = actorType,
        email = email,
        phone = phone,
        description = description,
        reservantType = reservantType,
        billingAddress = billingAddress
    )
}