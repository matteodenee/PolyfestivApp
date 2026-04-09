package com.example.clicker.ui.screens.festivalDetail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.festival.FestivalsRepository
import kotlinx.coroutines.launch

class FestivalDetailViewModel(
    private val festivalsRepository: FestivalsRepository
) : ViewModel() {

    private val internalState = mutableStateOf<FestivalDetailUiState>(FestivalDetailUiState.Loading)
    val state: State<FestivalDetailUiState> = internalState

    private var currentFestivalId: Int? = null

    fun loadFestival(festivalId: Int) {
        currentFestivalId = festivalId

        viewModelScope.launch {
            internalState.value = FestivalDetailUiState.Loading
            try {
                val festival = festivalsRepository.getFestivalById(festivalId)
                Log.d(TAG, "Détail chargé pour festival id=$festivalId")
                internalState.value = FestivalDetailUiState.Success(festival)
            } catch (e: Exception) {
                Log.e(TAG, "Erreur chargement détail festival", e)
                internalState.value = FestivalDetailUiState.Error("Impossible de charger le festival")
            }
        }
    }

    fun deleteFestival(onDeleted: () -> Unit) {
        val festivalId = currentFestivalId
        if (festivalId == null) {
            internalState.value = FestivalDetailUiState.Error("Id du festival introuvable")
            return
        }

        viewModelScope.launch {
            try {
                festivalsRepository.deleteFestival(festivalId)
                Log.d(TAG, "Festival supprimé id=$festivalId")
                onDeleted()
            } catch (e: Exception) {
                Log.e(TAG, "Erreur suppression festival", e)
                internalState.value = FestivalDetailUiState.Error("Suppression impossible")
            }
        }
    }
}
