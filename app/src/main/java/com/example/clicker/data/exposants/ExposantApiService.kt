package com.example.clicker.data.exposants

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExposantApiService {

    @GET("api/actors")
    suspend fun getExposants(): List<ActorDto>

    @GET("api/actors/{id}")
    suspend fun getExposantById(
        @Path("id") id: Int
    ): ActorDto

    @GET("api/actor-festivals/{festivalId}/actors/links")
    suspend fun getExposantLinksByFestival(
        @Path("festivalId") festivalId: Int
    ): List<ActorFestivalLinkDto>

    @POST("api/actors")
    suspend fun addExposant(
        @Body body: ActorRequest
    ): ActorDto

    @POST("api/actors/{id}")
    suspend fun updateExposant(
        @Path("id") id: Int,
        @Body body: ActorRequest
    ): ActorDto

    @DELETE("api/actors/{id}")
    suspend fun deleteExposant(
        @Path("id") id: Int
    ): Response<Unit>
}