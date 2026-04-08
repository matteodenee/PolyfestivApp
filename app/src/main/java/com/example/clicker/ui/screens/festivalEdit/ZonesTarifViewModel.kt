package com.example.clicker.ui.screens.festivalEdit

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.zone.TariffZoneDto
import com.example.clicker.data.zone.TariffZonesRepository
import kotlinx.coroutines.launch

sealed interface ZonesTarifUiState {
    data class Success(val zones: List<TariffZoneDto>) : ZonesTarifUiState
    data class Error(val message: String) : ZonesTarifUiState
    data object Loading : ZonesTarifUiState
}

class ZonesTarifViewModel(private val repository: TariffZonesRepository) : ViewModel() {

    private val _uiState = mutableStateOf<ZonesTarifUiState>(ZonesTarifUiState.Loading)
    val uiState: State<ZonesTarifUiState> = _uiState

    private var currentFestivalId: Int? = null

    fun loadZones(festivalId: Int) {
        currentFestivalId = festivalId
        fetchZones()
    }

    private fun fetchZones() {
        val fId = currentFestivalId ?: return
        viewModelScope.launch {
            _uiState.value = ZonesTarifUiState.Loading
            try {
                val zones = repository.getTariffZonesByFestival(fId)
                _uiState.value = ZonesTarifUiState.Success(zones)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading tariff zones", e)
                _uiState.value = ZonesTarifUiState.Error("Impossible de charger les zones tarifaires: ${e.message}")
            }
        }
    }

    fun updateZone(zone: TariffZoneDto) {
        viewModelScope.launch {
            try {
                repository.updateTariffZone(zone.id, zone)
                fetchZones()
            } catch (e: Exception) {
                Log.e(TAG, "Error updating tariff zone", e)
            }
        }
    }
}
