package com.example.clicker.ui.screens.games

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.game.GamesRepository
import kotlinx.coroutines.launch

class GamesViewModel(
    private val gamesRepository: GamesRepository
) : ViewModel() {

    private val internalState = mutableStateOf<GamesUiState>(GamesUiState.Loading)
    val state: State<GamesUiState> = internalState

    init {
        loadGames()
    }

    fun loadGames() {
        viewModelScope.launch {
            internalState.value = GamesUiState.Loading
            try {
                val games = gamesRepository.getGames()
                Log.d(TAG, "Jeux chargés : ${games.size}")
                internalState.value = GamesUiState.Success(games)
            } catch (e: Exception) {
                Log.e(TAG, "Erreur chargement jeux", e)
                internalState.value = GamesUiState.Error("Impossible de charger les jeux")
            }
        }
    }
}
