package com.example.clicker.ui.screens.festivalList

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.festival.FestivalsRepository
import kotlinx.coroutines.launch

class FestivalListViewModel(
    private val festivalsRepository: FestivalsRepository
) : ViewModel() {

    private val internalState = mutableStateOf<FestivalListUiState>(FestivalListUiState.Loading)
    val state: State<FestivalListUiState> = internalState

    init {
        loadFestivals()
    }

    fun loadFestivals() {
        viewModelScope.launch {
            internalState.value = FestivalListUiState.Loading
            try {
                val festivals = festivalsRepository.getFestivals()
                internalState.value = FestivalListUiState.Success(festivals)
            } catch (e: Exception) {
                Log.e("FestivalViewModel", "Erreur chargement festivals", e)
                internalState.value = FestivalListUiState.Error("Impossible de charger les festivals")
            }
        }
    }
}
