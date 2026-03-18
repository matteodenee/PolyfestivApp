package com.example.clicker.data.remote.api

import com.example.clicker.data.remote.dto.ActorDto
import com.example.clicker.data.remote.dto.ActorRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ExposantApiService {

    @GET("actors")
    suspend fun getExposants(): List<ActorDto>

    @GET("actors/{id}")
    suspend fun getExposantById(
        @Path("id") id: Int
    ): ActorDto

    @POST("actors")
    suspend fun addExposant(
        @Body body: ActorRequest
    ): ActorDto

    @POST("actors/{id}")
    suspend fun updateExposant(
        @Path("id") id: Int,
        @Body body: ActorRequest
    ): ActorDto

    @DELETE("actors/{id}")
    suspend fun deleteExposant(
        @Path("id") id: Int
    ): Response<Unit>
}