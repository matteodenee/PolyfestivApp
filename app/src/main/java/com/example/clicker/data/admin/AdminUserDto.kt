package com.example.clicker.data.admin

import kotlinx.serialization.Serializable

@Serializable
data class AdminUserDto(
    val id: Int,
    val login: String,
    val role: String,
    val validated: Boolean,
    val permissions: List<String> = emptyList()
)