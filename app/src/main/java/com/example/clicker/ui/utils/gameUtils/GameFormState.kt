package com.example.clicker.ui.utils.gameUtils
data class GameFormState(
    val name: String = "",
    val author: String = "",
    val nbMinPlayers: String = "1",
    val nbMaxPlayers: String = "4",
    val type: String = "",
    val ageMin: String = "0",
    val editorId: String = "",
    val description: String = "",
    val notice: String = "",
    val prototype: Boolean = false,
    val duree: String = "0",
    val imageUrl: String = "",
    val videoRulesUrl: String = ""
)