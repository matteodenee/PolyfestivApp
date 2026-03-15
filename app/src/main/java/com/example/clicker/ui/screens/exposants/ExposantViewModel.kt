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

    fun openDetails(exposantId: Int) {
        _uiState.update { current ->
            current.copy(
                selectedExposantId = exposantId,
                currentPage = ExposantsPage.DETAIL
            )
        }
    }

    fun openCreateForm() {
        _uiState.update { current ->
            current.copy(
                currentPage = ExposantsPage.FORM,
                formMode = ExposantFormMode.CREATE,
                selectedExposantId = null
            )
        }
    }

    fun openEditForm() {
        _uiState.update { current ->
            current.copy(
                currentPage = ExposantsPage.FORM,
                formMode = ExposantFormMode.EDIT
            )
        }
    }

    fun back() {
        when (_uiState.value.currentPage) {
            ExposantsPage.LIST -> Unit

            ExposantsPage.DETAIL -> {
                _uiState.update { current ->
                    current.copy(
                        currentPage = ExposantsPage.LIST,
                        selectedExposantId = null
                    )
                }
            }

            ExposantsPage.FORM -> {
                _uiState.update { current ->
                    current.copy(
                        currentPage = if (current.formMode == ExposantFormMode.EDIT) {
                            ExposantsPage.DETAIL
                        } else {
                            ExposantsPage.LIST
                        }
                    )
                }
            }
        }
    }

    fun saveExposant(formData: ExposantFormData) {
        viewModelScope.launch {
            val role = formData.role.trim().uppercase().ifBlank { "PUBLISHER" }
            val actorTypes = listOf(role)

            if (_uiState.value.formMode == ExposantFormMode.CREATE) {
                val newExposant = Exposant(
                    id = 0,
                    name = formData.name.trim(),
                    actorType = actorTypes,
                    phone = formData.phone.trim().ifBlank { null },
                    email = formData.email.trim().ifBlank { null },
                    description = formData.description.trim().ifBlank { null }
                )

                repository.addExposant(newExposant)
                val exposants = repository.getExposants()

                _uiState.update { current ->
                    current.copy(
                        exposants = exposants,
                        currentPage = ExposantsPage.LIST,
                        formMode = ExposantFormMode.CREATE,
                        selectedExposantId = null
                    )
                }
            } else {
                val selected = _uiState.value.selectedExposant ?: return@launch

                val updatedExposant = selected.copy(
                    name = formData.name.trim(),
                    actorType = actorTypes,
                    phone = formData.phone.trim().ifBlank { null },
                    email = formData.email.trim().ifBlank { null },
                    description = formData.description.trim().ifBlank { null }
                )

                repository.updateExposant(updatedExposant)
                val exposants = repository.getExposants()

                _uiState.update { current ->
                    current.copy(
                        exposants = exposants,
                        currentPage = ExposantsPage.DETAIL
                    )
                }
            }
        }
    }

    fun deleteSelectedExposant() {
        val selectedId = _uiState.value.selectedExposantId ?: return

        viewModelScope.launch {
            repository.deleteExposant(selectedId)
            val exposants = repository.getExposants()

            _uiState.update { current ->
                current.copy(
                    exposants = exposants,
                    currentPage = ExposantsPage.LIST,
                    selectedExposantId = null
                )
            }
        }
    }
}
