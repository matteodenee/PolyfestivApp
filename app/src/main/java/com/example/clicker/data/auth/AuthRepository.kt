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

        val setCookieHeader = httpResponse.headers()["Set-Cookie"]

        return LoginResult(
            response = body,
            cookie = setCookieHeader
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