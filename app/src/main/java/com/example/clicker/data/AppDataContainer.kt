package com.example.clicker.data

import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.festival.FestivalsRepository
import com.example.clicker.data.network.RetrofitInstance


class AppDataContainer : AppContainer {

    override val authRepository: AuthRepository by lazy {
        AuthRepository(RetrofitInstance.authApi)
    }

    override val gamesRepository: GamesRepository by lazy {
        GamesRepository(RetrofitInstance.gamesApi)
    }

    override val festivalsRepository: FestivalsRepository by lazy {
        FestivalsRepository(RetrofitInstance.festivalsApi)
    }
}
