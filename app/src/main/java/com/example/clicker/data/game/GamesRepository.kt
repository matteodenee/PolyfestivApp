package com.example.clicker.data.game

class GamesRepository(private val api: GamesApiService) {

    suspend fun getGames(): List<GameDto> =
        api.getGames()

    suspend fun getGameById(id: Int): GameDto =
        api.getGameById(id)

    suspend fun createGame(request: GameRequest): GameDto =
        api.createGame(request)

    suspend fun updateGame(id: Int, request: GameRequest): GameDto {
        return api.updateGame(id, request)
    }

    suspend fun deleteGame(id: Int) =
        api.deleteGame(id)
}