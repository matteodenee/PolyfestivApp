package com.example.clicker.data

import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.auth.RetrofitInstance


class AppDataContainer : AppContainer {

    override val authRepository: AuthRepository by lazy {
        AuthRepository(RetrofitInstance.api)
    }
}