package com.example.clicker.data.game

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GamesApiService {

    @GET("api/games")
    suspend fun getGames(): List<GameDto>

    @GET("api/games/{id}")
    suspend fun getGameById(@Path("id") id: Int): GameDto

    @GET("api/reservation-games")
    suspend fun getGamesByFestival(
        @Query("festivalId") festivalId: Int
    ): List<ReservationGameDto>

    @POST("api/games")
    suspend fun createGame(@Body request: GameRequest): GameDto

    @POST("api/games/{id}")
    suspend fun updateGame(
        @Path("id") id: Int,
        @Body request: GameRequest
    ): GameDto

    @DELETE("api/games/{id}")
    suspend fun deleteGame(@Path("id") id: Int)
}