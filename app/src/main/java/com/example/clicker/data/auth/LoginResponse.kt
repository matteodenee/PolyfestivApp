package com.example.clicker.data.auth

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val user: UserDto
)