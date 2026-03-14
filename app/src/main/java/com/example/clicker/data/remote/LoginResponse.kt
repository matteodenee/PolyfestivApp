package com.example.clicker.data.remote

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
    val message: String,
    val user: UserDto
)