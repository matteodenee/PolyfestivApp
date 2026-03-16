package com.example.clicker.data.repository

import com.example.clicker.data.model.Exposant
import kotlinx.coroutines.delay

interface ExposantRepository {
    suspend fun getExposants(): List<Exposant>
    suspend fun getExposantById(id: Int): Exposant?
    suspend fun addExposant(exposant: Exposant): Exposant
    suspend fun updateExposant(exposant: Exposant): Exposant?
    suspend fun deleteExposant(id: Int): Boolean
}

class FakeExposantRepository : ExposantRepository {

    private val exposants = mutableListOf(
        Exposant(
            id = 1,
            name = "Acteur 1",
            actorType = listOf("PUBLISHER"),
            phone = "06 11 22 33 44",
            email = "acteur1@festival.fr",
            description = "Editeur de jeux familiaux",
        ),
        Exposant(
            id = 2,
            name = "Acteur 2",
            actorType = listOf("SHOP"),
            phone = "06 55 44 33 22",
            email = "acteur2@festival.fr",
            description = "Boutique spécialisée jeux de plateau",
        ),
        Exposant(
            id = 3,
            name = "Acteur 3",
            actorType = listOf("PUBLISHER"),
            phone = "07 98 12 45 66",
            email = "acteur3@festival.fr",
            description = "Maison d’édition indépendante",
        ),
    )

    override suspend fun getExposants(): List<Exposant> {
        delay(200)
        return exposants.sortedBy { it.name }
    }

    override suspend fun getExposantById(id: Int): Exposant? {
        delay(100)
        return exposants.firstOrNull { it.id == id }
    }

    override suspend fun addExposant(exposant: Exposant): Exposant {
        delay(150)
        val nextId = (exposants.maxOfOrNull { it.id } ?: 0) + 1
        val newExposant = exposant.copy(id = nextId)
        exposants.add(newExposant)
        return newExposant
    }

    override suspend fun updateExposant(exposant: Exposant): Exposant? {
        delay(150)
        val index = exposants.indexOfFirst { it.id == exposant.id }
        if (index == -1) return null
        exposants[index] = exposant
        return exposant
    }

    override suspend fun deleteExposant(id: Int): Boolean {
        delay(150)
        return exposants.removeAll { it.id == id }
    }
}

object ExposantRepositoryProvider {
    val repository: ExposantRepository by lazy { FakeExposantRepository() }
}