package com.example.clicker.data.game

import com.example.clicker.data.local.game.GameDao
import com.example.clicker.data.local.game.toDto
import com.example.clicker.data.local.game.toEntity

class GamesRepository(
    private val api: GamesApiService,
    private val gameDao: GameDao
) {

    suspend fun getGames(): List<GameDto> {
        val remoteGames = api.getGames()
        gameDao.insertAllGames(remoteGames.map { it.toEntity() })
        return remoteGames
    }

    suspend fun getLocalGames(): List<GameDto> { // lire les jeux sans internet
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