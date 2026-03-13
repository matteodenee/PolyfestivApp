package com.example.clicker.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainScreenViewModel : ViewModel() {
    var _uiState = MutableStateFlow(MainScreenUiState())
        private set

    val uiState: StateFlow<MainScreenUiState> = _uiState.asStateFlow()


    fun increment() {
        // uiState = uiState.copy(currentScore = uiState.currentScore + 1);
        _uiState.update {
            current -> current.copy(currentScore = current.currentScore + 1)
        }
    }

    fun decrement() {
        // uiState = uiState.copy(currentScore = uiState.currentScore - 1);
        _uiState.update {
            current -> current.copy(currentScore = current.currentScore - 1)
        }
    }
}