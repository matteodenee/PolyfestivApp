package com.example.clicker.data.game

import kotlinx.serialization.Serializable

@Serializable
data class GameDto( // ce que le serveur renvoie
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