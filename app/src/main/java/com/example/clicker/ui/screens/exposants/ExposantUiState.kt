package com.example.clicker.ui.screens.exposants

import com.example.clicker.data.model.Exposant

data class ExposantUiState(
    val isLoading: Boolean = true,
    val exposants: List<Exposant> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null
) {
    val filteredExposants: List<Exposant>
        get() {
            if (searchQuery.isBlank()) return exposants

            return exposants.filter { exposant ->
                exposant.name.contains(searchQuery, ignoreCase = true) ||
                    exposant.actorType.any { it.contains(searchQuery, ignoreCase = true) } ||
                    exposant.description.orEmpty().contains(searchQuery, ignoreCase = true)
            }
        }

    fun findExposantById(id: Int): Exposant? {
        return exposants.firstOrNull { it.id == id }
    }
}