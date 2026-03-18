package com.example.clicker.data


import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.festival.FestivalsRepository

interface AppContainer {
    val authRepository: AuthRepository
    val gamesRepository: GamesRepository
    val festivalsRepository: FestivalsRepository
}
