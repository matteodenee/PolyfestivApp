package com.example.clicker.data.table

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TablesApiService {
    @GET("api/tables")
    suspend fun getTablesByFestival(@Query("festivalId") festivalId: Int): List<TableDto>

    @POST("api/tables")
    suspend fun createTable(@Body request: TableRequest): TableDto

    @POST("api/tables/{id}")
    suspend fun updateTable(@Path("id") id: Int, @Body request: TableDto): TableDto

    @DELETE("api/tables/{id}")
    suspend fun deleteTable(@Path("id") id: Int)
}
