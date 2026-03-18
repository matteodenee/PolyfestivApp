package com.example.clicker.data

import android.content.Context
import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.local.game.GameDatabase
import com.example.clicker.data.network.RetrofitInstance

class AppDataContainer(
    private val context: Context
) : AppContainer {

    override val authRepository: AuthRepository by lazy {
        AuthRepository(RetrofitInstance.authApi)
    }

    override val gamesRepository: GamesRepository by lazy {
        GamesRepository(
            api = RetrofitInstance.gamesApi,
            gameDao = GameDatabase.getDatabase(context).gameDao()
        )
    }
}