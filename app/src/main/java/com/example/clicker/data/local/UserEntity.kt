package com.example.clicker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: Int,
    val login: String,
    val role: String,
    val validated: Boolean = false
)