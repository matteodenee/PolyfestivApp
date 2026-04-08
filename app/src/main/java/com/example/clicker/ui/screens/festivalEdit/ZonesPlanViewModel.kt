package com.example.clicker.ui.screens.festivalEdit

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.zone.MapZoneDto
import com.example.clicker.data.zone.MapZonesRepository
import kotlinx.coroutines.launch

sealed interface ZonesPlanUiState {
    data class Success(val zones: List<MapZoneDto>) : ZonesPlanUiState
    data class Error(val message: String) : ZonesPlanUiState
    data object Loading : ZonesPlanUiState
}

class ZonesPlanViewModel(private val repository: MapZonesRepository) : ViewModel() {

    private val _uiState = mutableStateOf<ZonesPlanUiState>(ZonesPlanUiState.Loading)
    val uiState: State<ZonesPlanUiState> = _uiState

    private var currentFestivalId: Int? = null

    fun loadZones(festivalId: Int) {
        currentFestivalId = festivalId
        fetchZones()
    }

    private fun fetchZones() {
        val fId = currentFestivalId ?: return
        viewModelScope.launch {
            _uiState.value = ZonesPlanUiState.Loading
            try {
                val zones = repository.getMapZonesByFestival(fId)
                _uiState.value = ZonesPlanUiState.Success(zones)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading map zones", e)
                _uiState.value = ZonesPlanUiState.Error("Impossible de charger les zones du plan: ${e.message}")
            }
        }
    }

    fun updateZone(zone: MapZoneDto) {
        viewModelScope.launch {
            try {
                repository.updateMapZone(zone.id, zone)
                fetchZones()
            } catch (e: Exception) {
                Log.e(TAG, "Error updating map zone", e)
            }
        }
    }
}
