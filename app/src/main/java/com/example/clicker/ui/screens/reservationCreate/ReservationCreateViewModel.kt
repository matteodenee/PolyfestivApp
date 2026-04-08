package com.example.clicker.ui.screens.reservationCreate

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.exposants.Exposant
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.data.exposants.typeLabel
import com.example.clicker.data.festival.FestivalsRepository
import com.example.clicker.data.reservation.ReservationRequest
import com.example.clicker.data.reservation.ReservationsRepository
import kotlinx.coroutines.launch

class ReservationCreateViewModel(
    private val exposantRepository: ExposantRepository,
    private val reservationsRepository: ReservationsRepository,
    private val festivalsRepository: FestivalsRepository
) : ViewModel() {

    private val internalState =
        mutableStateOf<ReservationCreateUiState>(ReservationCreateUiState.Loading)
    val state: State<ReservationCreateUiState> = internalState

    var exposants by mutableStateOf<List<Exposant>>(emptyList())
        private set

    var festivalName by mutableStateOf("")
        private set

    var searchQuery by mutableStateOf("")
        private set

    var selectedExposant by mutableStateOf<Exposant?>(null)
        private set

    fun loadData(festivalId: Int) {
        viewModelScope.launch {
            internalState.value = ReservationCreateUiState.Loading
            try {
                exposants = exposantRepository.getExposants()
                val festival = festivalsRepository.getFestivalById(festivalId)
                festivalName = festival.name
                internalState.value = ReservationCreateUiState.Success
            } catch (e: Exception) {
                internalState.value = ReservationCreateUiState.Error(
                    e.message ?: "Impossible de charger les données"
                )
            }
        }
    }

    fun updateSearchQuery(query: String) {
        searchQuery = query
    }

    fun selectExposant(exposant: Exposant) {
        selectedExposant = exposant
        searchQuery = exposant.name
    }

    fun filteredExposants(): List<Exposant> {
        val query = searchQuery.trim().lowercase()
        if (query.isBlank()) return emptyList()

        return exposants.filter { exposant ->
            exposant.name.lowercase().contains(query) ||
                    exposant.typeLabel().lowercase().contains(query) ||
                    (exposant.email?.lowercase()?.contains(query) == true)
        }
    }

    fun createReservation(festivalId: Int, onCreated: () -> Unit) {
        val exposant = selectedExposant
        if (exposant == null) {
            internalState.value = ReservationCreateUiState.Error("Sélectionne un réservant")
            return
        }
        viewModelScope.launch {
            try {
                reservationsRepository.createReservation(
                    ReservationRequest(
                        festivalId = festivalId,
                        reservantId = exposant.id,
                        status = 0
                    )
                )
                onCreated()
            } catch (e: Exception) {
                internalState.value = ReservationCreateUiState.Error(
                    e.message ?: "Création de la réservation impossible"
                )
            }
        }
    }
}