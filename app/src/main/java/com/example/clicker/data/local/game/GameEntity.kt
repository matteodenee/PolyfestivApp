package com.example.clicker.data.local.game

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.clicker.data.game.GameDto

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val author: String,
    val nbMinPlayers: Int,
    val nbMaxPlayers: Int,
    val type: String,
    val ageMin: Int,
    val editorId: Int,
    val description: String,
    val notice: String,
    val prototype: Boolean,
    val duree: Int,
    val imageUrl: String,
    val videoRulesUrl: String
)

fun GameDto.toEntity(): GameEntity {
    return GameEntity(
        id = id,
        name = name,
        author = author,
        nbMinPlayers = nbMinPlayers,
        nbMaxPlayers = nbMaxPlayers,
        type = type,
        ageMin = ageMin,
        editorId = editorId,
        description = description,
        notice = notice,
        prototype = prototype,
        duree = duree,
        imageUrl = imageUrl,
        videoRulesUrl = videoRulesUrl
    )
}

fun GameEntity.toDto(): GameDto {
    return GameDto(
        id = id,
        name = name,
        author = author,
        nbMinPlayers = nbMinPlayers,
        nbMaxPlayers = nbMaxPlayers,
        type = type,
        ageMin = ageMin,
        editorId = editorId,
        description = description,
        notice = notice,
        prototype = prototype,
        duree = duree,
        imageUrl = imageUrl,
        videoRulesUrl = videoRulesUrl
    )
}