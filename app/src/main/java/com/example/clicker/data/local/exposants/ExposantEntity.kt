package com.example.clicker.data.local.exposant

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.clicker.data.exposants.ActorDto

@Entity(tableName = "exposants")
data class ExposantEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val type: List<String>,
    val description: String?,
    val email: String?,
    val phone: String?
)

fun ActorDto.toEntity(): ExposantEntity {
    return ExposantEntity(
        id = id,
        name = name,
        type = type,
        description = description,
        email = email,
        phone = phone
    )
}

fun ExposantEntity.toDto(): ActorDto {
    return ActorDto(
        id = id,
        name = name,
        type = type,
        description = description,
        email = email,
        phone = phone
    )
}