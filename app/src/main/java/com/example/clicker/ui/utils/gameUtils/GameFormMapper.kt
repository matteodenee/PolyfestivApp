package com.example.clicker.ui.utils.gameUtils

import com.example.clicker.data.game.GameDto
import com.example.clicker.data.game.GameRequest

fun GameFormState.toGameRequest(id: Int? = null): GameRequest {
    return GameRequest(
        id = id,
        name = name.trim(),
        author = author.trim(),
        nbMinPlayers = nbMinPlayers.toIntOrNull() ?: 1,
        nbMaxPlayers = nbMaxPlayers.toIntOrNull() ?: 4,
        type = type.trim(),
        ageMin = ageMin.toIntOrNull() ?: 0,
        editorId = editorId.toIntOrNull() ?: 0,
        description = description.trim(),
        notice = notice.trim(),
        prototype = prototype,
        duree = duree.toIntOrNull() ?: 0,
        imageUrl = imageUrl.trim(),
        videoRulesUrl = videoRulesUrl.trim()
    )
}

fun GameDto.toFormState(): GameFormState {
    return GameFormState(
        name = name,
        author = author,
        nbMinPlayers = nbMinPlayers.toString(),
        nbMaxPlayers = nbMaxPlayers.toString(),
        type = type,
        ageMin = ageMin.toString(),
        editorId = editorId.toString(),
        description = description,
        notice = notice,
        prototype = prototype,
        duree = duree.toString(),
        imageUrl = imageUrl,
        videoRulesUrl = videoRulesUrl
    )
}
