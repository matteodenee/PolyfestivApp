package com.example.clicker.ui.screens.gameDetail

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.game.GamesRepository
import kotlinx.coroutines.launch

class GameDetailViewModel(
    private val gamesRepository: GamesRepository
) : ViewModel() {

    private val internalState = mutableStateOf<GameDetailUiState>(GameDetailUiState.Loading)
    val state: State<GameDetailUiState> = internalState

    private var currentGameId: Int? = null

    fun loadGame(gameId: Int) {
        currentGameId = gameId

        viewModelScope.launch {
            internalState.value = GameDetailUiState.Loading
            try {
                val game = gamesRepository.getGameById(gameId)
                Log.d(TAG, "Détail chargé pour jeu id=$gameId")
                internalState.value = GameDetailUiState.Success(game)
            } catch (e: Exception) {
                Log.e(TAG, "Erreur chargement détail", e)
                internalState.value = GameDetailUiState.Error("Impossible de charger le jeu")
            }
        }
    }

    fun deleteGame(onDeleted: () -> Unit) {
        val gameId = currentGameId
        if (gameId == null) {
            internalState.value = GameDetailUiState.Error("Id du jeu introuvable")
            return
        }

        viewModelScope.launch {
            try {
                gamesRepository.deleteGame(gameId)
                Log.d(TAG, "Jeu supprimé id=$gameId")
                onDeleted()
            } catch (e: Exception) {
                Log.e(TAG, "Erreur suppression", e)
                internalState.value = GameDetailUiState.Error("Suppression impossible")
            }
        }
    }
}