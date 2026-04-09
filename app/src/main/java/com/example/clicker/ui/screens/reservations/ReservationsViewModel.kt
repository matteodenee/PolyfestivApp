package com.example.clicker.ui.screens.reservations

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.data.festival.FestivalsRepository
import com.example.clicker.data.reservation.ReservationsRepository
import kotlinx.coroutines.launch

class ReservationsViewModel(
    private val reservationsRepository: ReservationsRepository,
    private val festivalsRepository: FestivalsRepository,
    private val exposantRepository: ExposantRepository
) : ViewModel() {

    private val internalState = mutableStateOf<ReservationsUiState>(ReservationsUiState.Loading)
    val state: State<ReservationsUiState> = internalState

    var festivalName = ""
        private set

    private var reservantNamesById: Map<Int, String> = emptyMap()

    fun loadReservations(festivalId: Int) {
        viewModelScope.launch {
            internalState.value = ReservationsUiState.Loading
            try {
                val festival = festivalsRepository.getFestivalById(festivalId)
                festivalName = festival.name

                val exposants = exposantRepository.getExposants()
                // on transforme une liste en map pour accéder rapidement au nom d’un exposant à partir de son id
                // cela évite de parcourir toute la liste à chaque fois qu’on veut afficher un nom
                // clé = id de l’exposant, valeur = nom de l’exposant
                reservantNamesById = exposants.associate { exposant ->
                    exposant.id to exposant.name
                }

                val reservations = reservationsRepository.getReservationsByFestival(festivalId)
                internalState.value = ReservationsUiState.Success(reservations)
            } catch (e: Exception) {
                internalState.value = ReservationsUiState.Error(
                    e.message ?: "Impossible de charger les réservations"
                )
            }
        }
    }

    fun getReservantName(reservantId: Int): String {
        return reservantNamesById[reservantId] ?: "Acteur #$reservantId"
    }
}