package com.example.clicker.ui.screens.festival

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.festival.FestivalsRepository
import kotlinx.coroutines.launch

class FestivalViewModel(
    private val festivalsRepository: FestivalsRepository
) : ViewModel() {

    private val internalState = mutableStateOf<FestivalUiState>(FestivalUiState.Loading)
    val state: State<FestivalUiState> = internalState

    init {
        loadFestivals()
    }

    fun loadFestivals() {
        viewModelScope.launch {
            internalState.value = FestivalUiState.Loading
            try {
                val festivals = festivalsRepository.getFestivals()
                internalState.value = FestivalUiState.Success(festivals)
            } catch (e: Exception) {
                Log.e("FestivalViewModel", "Erreur chargement festivals", e)
                internalState.value = FestivalUiState.Error("Impossible de charger les festivals")
            }
        }
    }
}
