package com.example.clicker.data


import com.example.clicker.data.auth.AuthRepository

interface AppContainer {
    val authRepository: AuthRepository
}