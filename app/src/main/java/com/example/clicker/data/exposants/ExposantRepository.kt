package com.example.clicker.data.exposants

import com.example.clicker.data.local.exposants.ExposantDao
import com.example.clicker.data.local.exposants.toDto
import com.example.clicker.data.local.exposants.toEntity
import retrofit2.HttpException

class ExposantRepository(
    private val api: ExposantApiService,
    private val exposantDao: ExposantDao
) {

    suspend fun getExposants(): List<Exposant> {
        return try {
            val remoteExposants = api.getExposants()
                .map { it.toExposant() }
                .sortedBy { it.name }

            exposantDao.insertAllExposants(
                remoteExposants.map { it.toActorDto().toEntity() }
            )

            remoteExposants
        } catch (_: Exception) {
            exposantDao.getAllExposants()
                .map { it.toDto().toExposant() }
                .sortedBy { it.name }
        }
    }

    suspend fun getExposantsByFestival(festivalId: Int): List<FestivalExposantItem> {
        val links = api.getExposantLinksByFestival(festivalId)
        val linksByActorId = links.associateBy { it.actorId }

        val exposants = linksByActorId.keys.mapNotNull { actorId ->
            runCatching { api.getExposantById(actorId) }
                .getOrNull()
                ?.toExposant()
                ?.also { exposant ->
                    exposantDao.insertExposant(exposant.toActorDto().toEntity())
                }
                ?.let { exposant ->
                    val link = linksByActorId[actorId]
                    FestivalExposantItem(
                        exposant = exposant,
                        contacted = link?.contacted == true,
                        status = link?.status
                    )
                }
        }

        return exposants.sortedBy { it.exposant.name.lowercase() }
    }

    suspend fun getLocalExposants(): List<Exposant> {
        return exposantDao.getAllExposants()
            .map { it.toDto().toExposant() }
            .sortedBy { it.name }
    }

    suspend fun getExposantById(id: Int): Exposant? {
        return try {
            val remoteExposant = api.getExposantById(id).toExposant()
            exposantDao.insertExposant(remoteExposant.toActorDto().toEntity())
            remoteExposant
        } catch (_: Exception) {
            exposantDao.getExposantById(id)?.toDto()?.toExposant()
        }
    }

    suspend fun getLocalExposantById(id: Int): Exposant? {
        return exposantDao.getExposantById(id)?.toDto()?.toExposant()
    }

    suspend fun addExposant(exposant: Exposant): Exposant {
        val createdExposant = api.addExposant(exposant.toActorRequest()).toExposant()
        exposantDao.insertExposant(createdExposant.toActorDto().toEntity())
        return createdExposant
    }

    suspend fun updateExposant(exposant: Exposant): Exposant? {
        return try {
            val updatedExposant = api
                .updateExposant(exposant.id, exposant.toActorRequest())
                .toExposant()

            exposantDao.insertExposant(updatedExposant.toActorDto().toEntity())
            updatedExposant
        } catch (_: HttpException) {
            null
        }
    }

    suspend fun deleteExposant(id: Int): Boolean {
        return try {
            val response = api.deleteExposant(id)
            if (response.isSuccessful) {
                exposantDao.deleteExposantById(id)
            }
            response.isSuccessful
        } catch (_: Exception) {
            false
        }
    }
}

fun ActorDto.toExposant(): Exposant {
    return Exposant(
        id = id,
        name = name,
        actorType = type,
        description = description,
        email = email,
        phone = phone,
        reservantType = null,
        billingAddress = null
    )
}

fun Exposant.toActorDto(): ActorDto {
    return ActorDto(
        id = id,
        name = name,
        type = actorType,
        description = description,
        email = email,
        phone = phone
    )
}

fun Exposant.toActorRequest(): ActorRequest {
    return ActorRequest(
        id = if (id == 0) null else id,
        name = name,
        type = actorType,
        description = description,
        email = email,
        phone = phone,
        reservantType = reservantType,
        billingAddress = billingAddress
    )
}