package com.example.clicker.ui.screens.gameDetail

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.ui.utils.network.NetworkUtils
import kotlinx.coroutines.launch

class GameDetailViewModel(
    private val gamesRepository: GamesRepository,
    private val context: Context
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
                Log.d(TAG, "Détail chargé depuis le back pour jeu id=$gameId")
                internalState.value = GameDetailUiState.Success(game)
            } catch (e: Exception) {
                Log.e(TAG, "Erreur chargement back, tentative Room", e)

                try {
                    val localGame = gamesRepository.getLocalGameById(gameId)

                    if (localGame != null) {
                        Log.d(TAG, "Détail chargé depuis Room pour jeu id=$gameId")
                        internalState.value = GameDetailUiState.Success(localGame)
                    } else {
                        internalState.value =
                            GameDetailUiState.Error("Impossible de charger le jeu")
                    }
                } catch (localException: Exception) {
                    Log.e(TAG, "Erreur chargement Room", localException)
                    internalState.value =
                        GameDetailUiState.Error("Impossible de charger le jeu")
                }
            }
        }
    }

    fun deleteGame(onDeleted: () -> Unit) {
        val gameId = currentGameId
        if (gameId == null) {
            internalState.value = GameDetailUiState.Error("Id du jeu introuvable")
            return
        }
        // On bloque la suppression hors ligne
        if (!NetworkUtils.isInternetAvailable(context)) {
            internalState.value = GameDetailUiState.Error("Mode hors ligne : suppression impossible")
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