package com.example.clicker.data.repository

import com.example.clicker.data.local.UserDao
import com.example.clicker.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

class OfflineAuthRepository(private val userDao: UserDao) : AuthRepository {

    override fun getCurrentUserStream(): Flow<UserEntity?> = userDao.getCurrentUser()

    override suspend fun insertUser(user: UserEntity) = userDao.insertUser(user)

    override suspend fun deleteAllUsers() = userDao.deleteAllUsers()
}