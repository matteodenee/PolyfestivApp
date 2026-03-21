package com.example.clicker.data

import android.content.Context
import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.data.network.RetrofitInstance

class AppDataContainer(
    private val context: Context
) : AppContainer {

    override val authRepository: AuthRepository by lazy {
        AuthRepository(RetrofitInstance.authApi)
    }

    override val exposantRepository: ExposantRepository by lazy {
        ExposantRepository(RetrofitInstance.exposantApi)
    }
}