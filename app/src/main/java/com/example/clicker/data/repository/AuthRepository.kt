package com.example.clicker.data.repository

import com.example.clicker.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getCurrentUserStream(): Flow<UserEntity?>

    suspend fun insertUser(user: UserEntity)

    suspend fun deleteAllUsers()
}