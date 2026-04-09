package com.example.clicker.data.game

import com.example.clicker.data.exposants.ExposantApiService
import com.example.clicker.data.local.game.GameDao
import com.example.clicker.data.local.game.toDto
import com.example.clicker.data.local.game.toEntity

class GamesRepository(
    private val api: GamesApiService,
    private val actorApi: ExposantApiService,
    private val gameDao: GameDao
) {

    suspend fun getGames(): List<GameDto> {
        val remoteGames = api.getGames()
        gameDao.insertAllGames(remoteGames.map { it.toEntity() })
        return remoteGames
    }

    suspend fun getLocalGames(): List<GameDto> {
        return gameDao.getAllGames().map { it.toDto() }
    }

    suspend fun getGameById(id: Int): GameDto {
        val remoteGame = api.getGameById(id)
        gameDao.insertGame(remoteGame.toEntity())
        return remoteGame
    }

    suspend fun getLocalGameById(id: Int): GameDto? {
        return gameDao.getGameById(id)?.toDto()
    }

    suspend fun getGamesByFestival(festivalId: Int): List<FestivalGameItem> {
        val links = api.getGamesByFestival(festivalId)

        val gameIds = links.map { it.gameId }.distinct()

        val gamesById = gameIds.mapNotNull { gameId ->
            runCatching { api.getGameById(gameId) }
                .getOrNull()
                ?.also { gameDao.insertGame(it.toEntity()) }
                ?.let { game -> gameId to game }
        }.toMap()

        val actorIds = buildSet {
            links.mapNotNullTo(this) { it.editorActorId }
            gamesById.values.mapNotNullTo(this) { it.editorId }
        }

        val actorNamesById = actorIds.mapNotNull { actorId ->
            runCatching { actorApi.getExposantById(actorId) }
                .getOrNull()
                ?.let { actor -> actorId to actor.name }
        }.toMap()

        return links.mapNotNull { link ->
            val game = gamesById[link.gameId] ?: return@mapNotNull null

            FestivalGameItem(
                game = game,
                editorName = link.editorActorId?.let { actorNamesById[it] }
                    ?: game.editorId?.let { actorNamesById[it] }
            )
        }
    }

    suspend fun createGame(request: GameRequest): GameDto {
        val createdGame = api.createGame(request)
        gameDao.insertGame(createdGame.toEntity())
        return createdGame
    }

    suspend fun updateGame(id: Int, request: GameRequest): GameDto {
        val updatedGame = api.updateGame(id, request)
        gameDao.insertGame(updatedGame.toEntity())
        return updatedGame
    }

    suspend fun deleteGame(id: Int) {
        api.deleteGame(id)
        gameDao.deleteGameById(id)
    }
}