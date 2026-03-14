package com.example.clicker.data.remote

class AuthRepository(private val api: AuthApiService) {

    suspend fun login(login: String, password: String): LoginResponse {
        return api.login(
            LoginRequest(
                login = login,
                password = password
            )
        )
    }
}