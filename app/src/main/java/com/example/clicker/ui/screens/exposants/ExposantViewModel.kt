package com.example.clicker.ui.screens.exposants

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.exposants.Exposant
import com.example.clicker.data.exposants.ExposantFormData
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.ui.utils.network.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExposantViewModel(
    private val repository: ExposantRepository,
    private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExposantUiState())
    val uiState: StateFlow<ExposantUiState> = _uiState.asStateFlow()

    init {
        loadExposants()
    }

    fun loadExposants() {
        viewModelScope.launch {
            val isOnline = NetworkUtils.isInternetAvailable(context)

            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    isOnline = isOnline,
                    errorMessage = null
                )
            }

            try {
                val exposants = repository.getExposants()
                Log.d(TAG, "Exposants chargés depuis le back : ${exposants.size}")
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        isOnline = true,
                        exposants = exposants,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Erreur chargement back, tentative Room", e)

                try {
                    val localExposants = repository.getLocalExposants()
                    if (localExposants.isNotEmpty()) {
                        Log.d(TAG, "Exposants chargés depuis Room : ${localExposants.size}")
                        _uiState.update { current ->
                            current.copy(
                                isLoading = false,
                                isOnline = false,
                                exposants = localExposants,
                                errorMessage = null
                            )
                        }
                    } else {
                        _uiState.update { current ->
                            current.copy(
                                isLoading = false,
                                isOnline = false,
                                errorMessage = "Impossible de charger les exposants et aucune donnée locale n'est disponible"
                            )
                        }
                    }
                } catch (localException: Exception) {
                    Log.e(TAG, "Erreur chargement Room", localException)
                    _uiState.update { current ->
                        current.copy(
                            isLoading = false,
                            isOnline = false,
                            errorMessage = "Impossible de charger les exposants"
                        )
                    }
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
        if (!NetworkUtils.isInternetAvailable(context)) {
            _uiState.update { current ->
                current.copy(
                    isLoading = false,
                    isOnline = false,
                    errorMessage = "Mode hors ligne : création impossible"
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    isOnline = true,
                    errorMessage = null
                )
            }

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
                Log.d(TAG, "Exposant créé : ${newExposant.name}")
                refreshExposants()
            } catch (e: Exception) {
                Log.e(TAG, "Erreur ajout exposant", e)
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = "Ajout impossible : ${e.message ?: "erreur inconnue"}"
                    )
                }
            }
        }
    }

    fun updateExposant(exposantId: Int, formData: ExposantFormData) {
        if (!NetworkUtils.isInternetAvailable(context)) {
            _uiState.update { current ->
                current.copy(
                    isLoading = false,
                    isOnline = false,
                    errorMessage = "Mode hors ligne : modification impossible"
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    isOnline = true,
                    errorMessage = null
                )
            }

            try {
                val currentExposant = try {
                    repository.getExposantById(exposantId)
                } catch (_: Exception) {
                    repository.getLocalExposantById(exposantId)
                }

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
                Log.d(TAG, "Exposant modifié id=$exposantId")
                refreshExposants()
            } catch (e: Exception) {
                Log.e(TAG, "Erreur modification exposant", e)
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = "Modification impossible : ${e.message ?: "erreur inconnue"}"
                    )
                }
            }
        }
    }

    fun deleteExposantById(exposantId: Int) {
        if (!NetworkUtils.isInternetAvailable(context)) {
            _uiState.update { current ->
                current.copy(
                    isLoading = false,
                    isOnline = false,
                    errorMessage = "Mode hors ligne : suppression impossible"
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { current ->
                current.copy(
                    isLoading = true,
                    isOnline = true,
                    errorMessage = null
                )
            }

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

                Log.d(TAG, "Exposant supprimé id=$exposantId")
                refreshExposants()
            } catch (e: Exception) {
                Log.e(TAG, "Erreur suppression exposant", e)
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        errorMessage = "Suppression impossible"
                    )
                }
            }
        }
    }

    private suspend fun refreshExposants() {
        try {
            val exposants = repository.getExposants()
            Log.d(TAG, "Refresh exposants depuis le back : ${exposants.size}")
            _uiState.update { current ->
                current.copy(
                    isLoading = false,
                    isOnline = true,
                    exposants = exposants,
                    errorMessage = null
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erreur refresh back, tentative Room", e)

            try {
                val localExposants = repository.getLocalExposants()
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        isOnline = false,
                        exposants = localExposants,
                        errorMessage = null
                    )
                }
            } catch (localException: Exception) {
                Log.e(TAG, "Erreur refresh Room", localException)
                _uiState.update { current ->
                    current.copy(
                        isLoading = false,
                        isOnline = false,
                        errorMessage = "Erreur lors de l'actualisation des exposants"
                    )
                }
            }
        }
    }
}