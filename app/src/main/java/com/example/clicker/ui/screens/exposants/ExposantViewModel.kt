package com.example.clicker.ui.screens.exposants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.model.Exposant
import com.example.clicker.data.model.ExposantFormData
import com.example.clicker.data.repository.ExposantRepository
import com.example.clicker.data.repository.ExposantRepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExposantViewModel(
    private val repository: ExposantRepository = ExposantRepositoryProvider.repository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExposantUiState())
    val uiState: StateFlow<ExposantUiState> = _uiState.asStateFlow()

    init {
        loadExposants()
    }

    fun loadExposants() {
        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            try {
                val exposants = repository.getExposants()
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        exposants = exposants
                    )
                }
            } catch (e: Exception) {
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Erreur lors du chargement des exposants"
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { current ->
            current.copy(searchQuery = query)
        }
    }

    fun saveNewExposant(formData: ExposantFormData) {
        viewModelScope.launch {
            val role = formData.role.trim().uppercase().ifBlank { "PUBLISHER" }

            val newExposant = Exposant(
                id = 0,
                name = formData.name.trim(),
                actorType = listOf(role),
                phone = formData.phone.trim().ifBlank { null },
                email = formData.email.trim().ifBlank { null },
                description = formData.description.trim().ifBlank { null }
            )

            repository.addExposant(newExposant)
            refreshExposants()
        }
    }

    fun updateExposant(exposantId: Int, formData: ExposantFormData) {
        viewModelScope.launch {
            val currentExposant = repository.getExposantById(exposantId) ?: return@launch
            val role = formData.role.trim().uppercase().ifBlank { "PUBLISHER" }

            val updatedExposant = currentExposant.copy(
                name = formData.name.trim(),
                actorType = listOf(role),
                phone = formData.phone.trim().ifBlank { null },
                email = formData.email.trim().ifBlank { null },
                description = formData.description.trim().ifBlank { null }
            )

            repository.updateExposant(updatedExposant)
            refreshExposants()
        }
    }

    fun deleteExposantById(exposantId: Int) {
        viewModelScope.launch {
            repository.deleteExposant(exposantId)
            refreshExposants()
        }
    }

    private suspend fun refreshExposants() {
        val exposants = repository.getExposants()
        _uiState.update { current ->
            current.copy(
                isLoading = false,
                exposants = exposants,
                errorMessage = null
            )
        }
    }
}
