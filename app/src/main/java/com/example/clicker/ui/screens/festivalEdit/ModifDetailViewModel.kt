package com.example.clicker.ui.screens.festivalEdit

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.festival.FestivalDto
import com.example.clicker.data.festival.FestivalRequest
import com.example.clicker.data.festival.FestivalsRepository
import kotlinx.coroutines.launch

sealed interface ModifDetailUiState {
    data class Success(val festival: FestivalDto) : ModifDetailUiState
    data class Error(val message: String) : ModifDetailUiState
    data object Loading : ModifDetailUiState
}

class ModifDetailViewModel(private val repository: FestivalsRepository) : ViewModel() {

    private val _uiState = mutableStateOf<ModifDetailUiState>(ModifDetailUiState.Loading)
    val uiState: State<ModifDetailUiState> = _uiState

    private var currentFestivalId: Int? = null

    fun loadFestival(festivalId: Int) {
        currentFestivalId = festivalId
        viewModelScope.launch {
            _uiState.value = ModifDetailUiState.Loading
            try {
                val festival = repository.getFestivalById(festivalId)
                _uiState.value = ModifDetailUiState.Success(festival)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading festival", e)
                _uiState.value = ModifDetailUiState.Error("Impossible de charger le festival")
            }
        }
    }

    fun updateFestival(festival: FestivalDto) {
        viewModelScope.launch {
            try {
                // Here we map Dto to Request (we use the exact fields necessary)
                val request = FestivalRequest(
                    name = festival.name,
                    creationDate = festival.creationDate,
                    description = festival.description,
                    startDate = festival.startDate,
                    endDate = festival.endDate
                )
                repository.updateFestival(festival.id, request)
                loadFestival(festival.id)
            } catch (e: Exception) {
                Log.e(TAG, "Error updating festival", e)
            }
        }
    }
}
