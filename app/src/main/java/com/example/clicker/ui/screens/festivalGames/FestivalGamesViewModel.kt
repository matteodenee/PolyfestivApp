package com.example.clicker.ui.screens.festivalGames

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.game.GamesRepository
import kotlinx.coroutines.launch

class FestivalGamesViewModel(
    private val gamesRepository: GamesRepository
) : ViewModel() {

    private val internalState =
        mutableStateOf<FestivalGamesUiState>(FestivalGamesUiState.Loading)
    val state: State<FestivalGamesUiState> = internalState

    fun loadGamesByFestival(festivalId: Int) {
        viewModelScope.launch {
            internalState.value = FestivalGamesUiState.Loading
            try {
                val games = gamesRepository.getGamesByFestival(festivalId)
                internalState.value = FestivalGamesUiState.Success(games)
            } catch (e: Exception) {
                Log.e(TAG, "Erreur chargement jeux du festival $festivalId", e)
                internalState.value = FestivalGamesUiState.Error(
                    e.message ?: "Impossible de charger les jeux du festival"
                )
            }
        }
    }
}