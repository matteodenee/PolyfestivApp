package com.example.clicker.data

import android.content.Context
import com.example.clicker.data.local.AppDatabase
import com.example.clicker.data.repository.AuthRepository
import com.example.clicker.data.repository.OfflineAuthRepository

interface AppContainer {
    val authRepository: AuthRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val authRepository: AuthRepository by lazy {
        OfflineAuthRepository(
            AppDatabase.getDatabase(context).userDao()
        )
    }
}