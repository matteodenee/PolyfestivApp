package com.example.clicker.ui.screens.exposants

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.exposants.Exposant
import com.example.clicker.data.exposants.ExposantFormData
import com.example.clicker.data.exposants.ExposantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExposantViewModel(
    private val repository: ExposantRepository
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
                        exposants = exposants,
                        errorMessage = null
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
            try {
                val role = formData.role.trim().uppercase().ifBlank { "PUBLISHER" }

                val newExposant = Exposant(
                    id = 0,
                    name = formData.name.trim(),
                    actorType = listOf(role),
                    phone = formData.phone.trim().ifBlank { null },
                    email = formData.email.trim().ifBlank { null },
                    description = formData.description.trim().ifBlank { null },
                    reservantType = null,
                    billingAddress = null
                )

                repository.addExposant(newExposant)
                refreshExposants()
            } catch (e: Exception) {
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Erreur lors de l'ajout de l'exposant"
                    )
                }
            }
        }
    }

    fun updateExposant(exposantId: Int, formData: ExposantFormData) {
        viewModelScope.launch {
            try {
                val currentExposant = repository.getExposantById(exposantId)

                if (currentExposant == null) {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            errorMessage = "Exposant introuvable"
                        )
                    }
                    return@launch
                }

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
            } catch (e: Exception) {
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Erreur lors de la modification de l'exposant"
                    )
                }
            }
        }
    }

    fun deleteExposantById(exposantId: Int) {
        viewModelScope.launch {
            try {
                val deleted = repository.deleteExposant(exposantId)

                if (!deleted) {
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            errorMessage = "Suppression impossible"
                        )
                    }
                    return@launch
                }

                refreshExposants()
            } catch (e: Exception) {
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Erreur lors de la suppression de l'exposant"
                    )
                }
            }
        }
    }

    private suspend fun refreshExposants() {
        try {
            val exposants = repository.getExposants()
            _uiState.update { current ->
                current.copy(
                    isLoading = false,
                    exposants = exposants,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            _uiState.update { current ->
                current.copy(
                    isLoading = false,
                    errorMessage = e.message ?: "Erreur lors de l'actualisation des exposants"
                )
            }
        }
    }
}