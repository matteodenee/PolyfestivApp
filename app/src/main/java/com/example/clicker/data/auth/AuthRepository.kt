package com.example.clicker.data.auth

class AuthRepository(private val api: AuthApiService) {

    suspend fun login(login: String, password: String): AuthResponse {
        return api.login(
            LoginRequest(
                login = login,
                password = password
            )
        )
    }

    suspend fun register(login: String, password: String): AuthResponse {
        return api.register(
            RegisterRequest(
                login = login,
                password = password
            )
        )
    }
}