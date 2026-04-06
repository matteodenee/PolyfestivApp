package com.example.clicker.ui.screens.festivalEdit

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.table.TableDto
import com.example.clicker.data.table.TableRequest
import com.example.clicker.data.table.TablesRepository
import kotlinx.coroutines.launch

sealed interface StockTablesUiState {
    data class Success(val tables: List<TableDto>) : StockTablesUiState
    data class Error(val message: String) : StockTablesUiState
    data object Loading : StockTablesUiState
}

class StockTablesViewModel(private val repository: TablesRepository) : ViewModel() {

    private val _uiState = mutableStateOf<StockTablesUiState>(StockTablesUiState.Loading)
    val uiState: State<StockTablesUiState> = _uiState

    private var currentFestivalId: Int? = null

    fun loadTables(festivalId: Int) {
        currentFestivalId = festivalId
        fetchTables()
    }

    private fun fetchTables() {
        val fId = currentFestivalId ?: return
        viewModelScope.launch {
            _uiState.value = StockTablesUiState.Loading
            try {
                val tables = repository.getTablesByFestival(fId)
                _uiState.value = StockTablesUiState.Success(tables)
            } catch (e: Exception) {
                Log.e(TAG, "Error loading tables", e)
                _uiState.value = StockTablesUiState.Error("Impossible de charger les tables: ${e.message}")
            }
        }
    }

    fun updateTable(table: TableDto) {
        viewModelScope.launch {
            try {
                repository.updateTable(table.id, table)
                fetchTables() // Reload lists to refresh UI immediately
            } catch (e: Exception) {
                Log.e(TAG, "Error updating table", e)
            }
        }
    }

    fun addTable(request: TableRequest) {
        viewModelScope.launch {
            try {
                repository.createTable(request)
                fetchTables() // Reload lists to refresh UI immediately
            } catch (e: Exception) {
                Log.e(TAG, "Error adding table", e)
            }
        }
    }
}
