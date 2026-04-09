package com.example.clicker.data.admin

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Body

interface AdminApiService {

    @GET("api/users")
    suspend fun getUsers(): List<AdminUserDto>

    @POST("api/users/{id}/validate")
    suspend fun validateUser(
        @Path("id") id: Int
    ): AdminUserDto

    @POST("api/users/{id}/role")
    suspend fun updateUserRole(
        @Path("id") id: Int,
        @Body request: UserRoleRequest
    ): AdminUserDto

    @POST("api/users/{id}/refuse")
    suspend fun deleteUser(
        @Path("id") id: Int
    )
}