package com.example.clicker.data.auth

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}