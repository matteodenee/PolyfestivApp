package com.example.clicker.data.auth

class AuthRepository(private val api: AuthApiService) {

    suspend fun login(login: String, password: String): LoginResult {
        val httpResponse = api.login(
            LoginRequest(
                login = login,
                password = password
            )
        )

        if (!httpResponse.isSuccessful) {
            throw Exception("Erreur HTTP ${httpResponse.code()}")
        }

        val body = httpResponse.body()
        if (body == null) {
            throw Exception("Réponse vide du serveur")
        }

        val cookies = httpResponse.headers().values("Set-Cookie")

        val accessCookie = cookies
            .find { it.startsWith("access_token=") }
            ?.substringBefore(";")

        val refreshCookie = cookies
            .find { it.startsWith("refresh_token=") }
            ?.substringBefore(";")

        return LoginResult(
            response = body,
            accessCookie = accessCookie,
            refreshCookie = refreshCookie
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

    suspend fun checkSession(): Boolean {
        val response = api.me()
        return response.isSuccessful
    }

    suspend fun refreshSession(): String? {
        val response = api.refresh()
        if (!response.isSuccessful) {
            return null
        }

        val cookies = response.headers().values("Set-Cookie")
        return cookies
            .find { it.startsWith("access_token=") }
            ?.substringBefore(";")
    }
}