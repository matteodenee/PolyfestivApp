package com.example.clicker.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val message: String,
    val user: UserDto
)