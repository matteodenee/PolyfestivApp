package com.example.clicker.data

import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.exposants.ExposantRepository

interface AppContainer {
    val authRepository: AuthRepository
    val exposantRepository: ExposantRepository
}