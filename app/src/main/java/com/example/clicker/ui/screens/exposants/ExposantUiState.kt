package com.example.clicker.ui.screens.exposants

import com.example.clicker.data.model.Exposant

enum class ExposantsPage {
    LIST,
    DETAIL,
    FORM,
}

enum class ExposantFormMode {
    CREATE,
    EDIT,
}

data class ExposantUiState(
    val isLoading: Boolean = true,
    val exposants: List<Exposant> = emptyList(),
    val searchQuery: String = "",
    val selectedExposantId: Int? = null,
    val currentPage: ExposantsPage = ExposantsPage.LIST,
    val formMode: ExposantFormMode = ExposantFormMode.CREATE,
    val errorMessage: String? = null,
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

    val selectedExposant: Exposant?
        get() = exposants.firstOrNull { it.id == selectedExposantId }
}