package com.example.clicker.ui.screens.festivalEdit

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.equipment.EquipmentRequest
import com.example.clicker.data.equipment.EquipmentsRepository
import kotlinx.coroutines.launch
import com.example.clicker.data.equipment.EquipmentDto

sealed interface StockMaterielUiState {
    data class Success(val equipments: List<EquipmentDto>) : StockMaterielUiState
    data class Error(val message: String) : StockMaterielUiState
    data object Loading : StockMaterielUiState
}

class StockMaterielViewModel(private val repository: EquipmentsRepository) : ViewModel() {

    private val _uiState = mutableStateOf<StockMaterielUiState>(StockMaterielUiState.Loading)
    val uiState: State<StockMaterielUiState> = _uiState

    private var currentFestivalId: Int? = null

    fun loadEquipments(festivalId: Int) {
        currentFestivalId = festivalId
        fetchEquipments()
    }

    private fun fetchEquipments() {
        val fId = currentFestivalId ?: return
        viewModelScope.launch {
            _uiState.value = StockMaterielUiState.Loading
            try {
                val equipments = repository.getEquipmentsByFestival(fId)
                _uiState.value = StockMaterielUiState.Success(equipments)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading equipments", e)
                _uiState.value = StockMaterielUiState.Error("Impossible de charger les équipements: ${e.message}")
            }
        }
    }

    fun updateEquipment(equipment: EquipmentDto) {
        viewModelScope.launch {
            try {
                repository.updateEquipment(equipment.id, equipment)
                fetchEquipments()
            } catch (e: Exception) {
                Log.e(TAG, "Error updating equipment", e)
            }
        }
    }

    fun addEquipment(request: EquipmentRequest) {
        viewModelScope.launch {
            try {
                repository.createEquipment(request)
                fetchEquipments()
            } catch (e: Exception) {
                Log.e(TAG, "Error adding equipment", e)
            }
        }
    }
}
