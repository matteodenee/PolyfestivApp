package com.example.clicker.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val id: Int,
    val login: String,
    val role: String,
    val validated: Boolean
)