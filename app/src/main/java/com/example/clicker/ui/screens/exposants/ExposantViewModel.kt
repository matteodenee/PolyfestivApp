package com.example.clicker.ui.screens.exposants

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.exposants.Exposant
import com.example.clicker.data.exposants.ExposantFormData
import com.example.clicker.data.exposants.ExposantRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExposantViewModel(
    private val repository: ExposantRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExposantUiState>(ExposantUiState.Loading)
    val uiState: StateFlow<ExposantUiState> = _uiState.asStateFlow()

    private val _festivalUiState =
        MutableStateFlow<FestivalExposantsUiState>(FestivalExposantsUiState.Loading)
    val festivalUiState: StateFlow<FestivalExposantsUiState> = _festivalUiState.asStateFlow()

    fun loadExposants() {
        viewModelScope.launch {
            _uiState.value = ExposantUiState.Loading

            try {
                val exposants = repository.getExposants()
                _uiState.value = ExposantUiState.Success(
                    exposants = exposants,
                    isOnline = true
                )
            } catch (e: Exception) {
                val localExposants = repository.getLocalExposants()

                if (localExposants.isNotEmpty()) {
                    _uiState.value = ExposantUiState.Success(
                        exposants = localExposants,
                        isOnline = false
                    )
                } else {
                    _uiState.value = ExposantUiState.Error(
                        e.message ?: "Erreur chargement exposants"
                    )
                }
            }
        }
    }

    fun loadExposantsByFestival(festivalId: Int) {
        viewModelScope.launch {
            _festivalUiState.value = FestivalExposantsUiState.Loading
            try {
                val exposants = repository.getExposantsByFestival(festivalId)
                _festivalUiState.value = FestivalExposantsUiState.Success(exposants)
            } catch (e: Exception) {
                _festivalUiState.value = FestivalExposantsUiState.Error(
                    e.message ?: "Erreur chargement exposants du festival"
                )
            }
        }
    }

    fun saveNewExposant(formData: ExposantFormData, onSuccess: () -> Unit = {}) {
        val state = _uiState.value
        if (state !is ExposantUiState.Success || !state.isOnline) return

        viewModelScope.launch {
            try {
                repository.addExposant(formDataToExposant(formData))
                loadExposants()
                onSuccess()
            } catch (_: Exception) {
                _uiState.value = ExposantUiState.Error("Erreur ajout exposant")
            }
        }
    }

    fun updateExposant(id: Int, formData: ExposantFormData, onSuccess: () -> Unit = {}) {
        val state = _uiState.value
        if (state !is ExposantUiState.Success || !state.isOnline) return

        viewModelScope.launch {
            try {
                repository.updateExposant(formDataToExposant(formData, id))
                loadExposants()
                onSuccess()
            } catch (_: Exception) {
                _uiState.value = ExposantUiState.Error("Erreur modification exposant")
            }
        }
    }

    fun deleteExposantById(id: Int, onSuccess: () -> Unit = {}) {
        val state = _uiState.value
        if (state !is ExposantUiState.Success || !state.isOnline) return

        viewModelScope.launch {
            try {
                repository.deleteExposant(id)
                loadExposants()
                onSuccess()
            } catch (_: Exception) {
                _uiState.value = ExposantUiState.Error("Erreur suppression exposant")
            }
        }
    }

    private fun formDataToExposant(
        formData: ExposantFormData,
        id: Int = 0
    ): Exposant {
        return Exposant(
            id = id,
            name = formData.name,
            actorType = listOf(formData.role),
            phone = formData.phone.ifBlank { null },
            email = formData.email.ifBlank { null },
            description = formData.description.ifBlank { null },
            reservantType = null,
            billingAddress = null
        )
    }
}