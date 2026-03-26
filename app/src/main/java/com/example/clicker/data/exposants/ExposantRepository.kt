package com.example.clicker.data.exposants

import retrofit2.HttpException

class ExposantRepository(
    private val api: ExposantApiService
) {

    suspend fun getExposants(): List<Exposant> {
        return api.getExposants()
            .map { it.toExposant() }
            .sortedBy { it.name }
    }

    suspend fun getExposantById(id: Int): Exposant? {
        return try {
            api.getExposantById(id).toExposant()
        } catch (_: HttpException) {
            null
        }
    }

    suspend fun addExposant(exposant: Exposant): Exposant {
        return api.addExposant(exposant.toActorRequest()).toExposant()
    }

    suspend fun updateExposant(exposant: Exposant): Exposant? {
        return try {
            api.updateExposant(exposant.id, exposant.toActorRequest()).toExposant()
        } catch (_: HttpException) {
            null
        }
    }

    suspend fun deleteExposant(id: Int): Boolean {
        return api.deleteExposant(id).isSuccessful
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