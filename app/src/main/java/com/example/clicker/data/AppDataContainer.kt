package com.example.clicker.data

import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.network.RetrofitInstance
import com.example.clicker.data.repository.ExposantRepository
import com.example.clicker.data.repository.RemoteExposantRepository

class AppDataContainer : AppContainer {

    override val authRepository: AuthRepository by lazy {
        AuthRepository(RetrofitInstance.authApi)
    }

    override val exposantRepository: ExposantRepository by lazy {
        RemoteExposantRepository(RetrofitInstance.exposantApi)
    }
}