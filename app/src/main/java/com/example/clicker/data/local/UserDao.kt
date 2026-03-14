package com.example.clicker.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // REPLACE: si un utilisateur avec la meme clé primaire existe déjà on le remplace (ex: reconnexion, changement role)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users LIMIT 1") // on veut le premier user (limit 1) car on veut l'utilisateur connecté
    fun getCurrentUser(): Flow<UserEntity?>

    @Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}