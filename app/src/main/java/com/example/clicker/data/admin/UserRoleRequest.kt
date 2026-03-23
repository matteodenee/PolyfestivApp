package com.example.clicker.data.admin

import kotlinx.serialization.Serializable

@Serializable
data class UserRoleRequest(
    val role: String
)