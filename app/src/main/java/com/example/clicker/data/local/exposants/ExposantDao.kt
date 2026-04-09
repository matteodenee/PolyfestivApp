package com.example.clicker.data.local.exposants

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExposantDao {

    @Query("SELECT * FROM exposants ORDER BY name ASC")
    suspend fun getAllExposants(): List<ExposantEntity>

    @Query("SELECT * FROM exposants WHERE id = :id")
    suspend fun getExposantById(id: Int): ExposantEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExposant(exposant: ExposantEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllExposants(exposants: List<ExposantEntity>)

    @Query("DELETE FROM exposants WHERE id = :id")
    suspend fun deleteExposantById(id: Int)
}