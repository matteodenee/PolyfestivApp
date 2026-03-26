package com.example.clicker.data.exposants

import com.example.clicker.data.local.exposant.ExposantDao
import com.example.clicker.data.local.exposant.toEntity
import com.example.clicker.data.local.exposant.toExposant
import retrofit2.HttpException

class ExposantRepository(
    private val api: ExposantApiService,
    private val exposantDao: ExposantDao
) {

    suspend fun getExposants(): List<Exposant> {
        val remoteExposants = api.getExposants()
            .map { it.toExposant() }
            .sortedBy { it.name }

        exposantDao.insertAllExposants(remoteExposants.map { it.toEntity() })
        return remoteExposants
    }

    suspend fun getLocalExposants(): List<Exposant> {
        return exposantDao.getAllExposants()
            .map { it.toExposant() }
            .sortedBy { it.name }
    }

    suspend fun getExposantById(id: Int): Exposant? {
        return try {
            val remoteExposant = api.getExposantById(id).toExposant()
            exposantDao.insertExposant(remoteExposant.toEntity())
            remoteExposant
        } catch (_: HttpException) {
            null
        }
    }

    suspend fun getLocalExposantById(id: Int): Exposant? {
        return exposantDao.getExposantById(id)?.toExposant()
    }

    suspend fun addExposant(exposant: Exposant): Exposant {
        val createdExposant = api.addExposant(exposant.toActorRequest()).toExposant()
        exposantDao.insertExposant(createdExposant.toEntity())
        return createdExposant
    }

    suspend fun updateExposant(exposant: Exposant): Exposant? {
        return try {
            val updatedExposant = api.updateExposant(exposant.id, exposant.toActorRequest()).toExposant()
            exposantDao.insertExposant(updatedExposant.toEntity())
            updatedExposant
        } catch (_: HttpException) {
            null
        }
    }

    suspend fun deleteExposant(id: Int): Boolean {
        val response = api.deleteExposant(id)
        if (response.isSuccessful) {
            exposantDao.deleteExposantById(id)
        }
        return response.isSuccessful
    }
}

fun ActorDto.toExposant(): Exposant {
    return Exposant(
        id = id,
        name = name,
        actorType = type,
        email = email,
        phone = phone,
        description = description,
        reservantType = null,
        billingAddress = null
    )
}

fun Exposant.toActorRequest(): ActorRequest {
    return ActorRequest(
        id = if (id == 0) null else id,
        name = name,
        type = actorType,
        email = email,
        phone = phone,
        description = description,
        reservantType = reservantType,
        billingAddress = billingAddress
    )
}