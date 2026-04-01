package com.example.clicker.ui.screens.festivalGames

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.game.GamesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FestivalGamesViewModel(
    private val gamesRepository: GamesRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<FestivalGamesUiState>(FestivalGamesUiState.Loading)

    val uiState: StateFlow<FestivalGamesUiState> = _uiState

    fun loadGamesByFestival(festivalId: Int) {
        viewModelScope.launch {
            _uiState.value = FestivalGamesUiState.Loading
            try {
                val games = gamesRepository.getGamesByFestival(festivalId)
                _uiState.value = FestivalGamesUiState.Success(games)
            } catch (e: Exception) {
                Log.e(TAG, "Erreur chargement jeux du festival $festivalId", e)
                _uiState.value = FestivalGamesUiState.Error(
                    e.message ?: "Impossible de charger les jeux du festival"
                )
            }
        }
    }
}