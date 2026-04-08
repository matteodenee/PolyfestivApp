package com.example.clicker.ui.screens.festivalList

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.festival.FestivalRequest
import com.example.clicker.data.festival.FestivalsRepository
import kotlinx.coroutines.launch

sealed interface FestivalCreateUiState {
    data object Idle : FestivalCreateUiState
    data object Loading : FestivalCreateUiState
    data class Success(val festivalId: Int) : FestivalCreateUiState
    data class Error(val message: String) : FestivalCreateUiState
}

class FestivalCreateViewModel(private val repository: FestivalsRepository) : ViewModel() {

    private val _uiState = mutableStateOf<FestivalCreateUiState>(FestivalCreateUiState.Idle)
    val uiState: State<FestivalCreateUiState> = _uiState

    fun createFestival(name: String, description: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            _uiState.value = FestivalCreateUiState.Loading
            try {
                // Formatting Date to default creation timestamp logic
                // Provide dummy dates if needed but we get YYYY-MM-DD from UI
                val request = FestivalRequest(
                    name = name,
                    description = description,
                    startDate = startDate,
                    endDate = endDate,
                    creationDate = startDate // Use start date for creation timestamp parity 
                )
                
                val created = repository.createFestival(request)
                // Navigation listener handles Success
                _uiState.value = FestivalCreateUiState.Success(created.id)
            } catch (e: Exception) {
                Log.e(TAG, "Error creating festival", e)
                _uiState.value = FestivalCreateUiState.Error("Impossible de créer le festival: ${e.message}")
            }
        }
    }
}
