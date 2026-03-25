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
                Log.d(TAG, "Jeux chargés depuis le back : ${games.size}")
                internalState.value = GamesUiState.Success(games)
            } catch (e: Exception) { // si le réseau échoue, on passe au local
                Log.e(TAG, "Erreur chargement back, tentative Room", e)
                try {
                    val localGames = gamesRepository.getLocalGames()
                    if (localGames.isNotEmpty()) {
                        Log.d(TAG, "Jeux chargés depuis Room : ${localGames.size}")
                        internalState.value = GamesUiState.Success(localGames)
                    } else {
                        internalState.value = GamesUiState.Error(
                            "Impossible de charger les jeux et aucune donnée locale n'est disponible"
                        )
                    }
                } catch (localException: Exception) {
                    Log.e(TAG, "Erreur chargement Room", localException)
                    internalState.value = GamesUiState.Error("Impossible de charger les jeux")
                }
            }
        }
    }
}