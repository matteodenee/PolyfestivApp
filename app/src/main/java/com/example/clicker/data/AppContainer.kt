package com.example.clicker.data

import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.repository.ExposantRepository

interface AppContainer {
    val authRepository: AuthRepository
    val exposantRepository: ExposantRepository
}