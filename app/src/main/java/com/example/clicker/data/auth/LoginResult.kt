package com.example.clicker.data.auth

data class LoginResult(
    val response: AuthResponse,
    val accessCookie: String?,
    val refreshCookie: String?
)