package com.example.clicker.ui.screens.gameEdit

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.ui.utils.gameUtils.GameFormState
import com.example.clicker.ui.utils.gameUtils.GameFormValidator
import com.example.clicker.ui.utils.gameUtils.toFormState
import com.example.clicker.ui.utils.gameUtils.toGameRequest
import com.example.clicker.ui.utils.network.NetworkUtils
import kotlinx.coroutines.launch

class GameEditViewModel(
    private val gamesRepository: GamesRepository,
    private val context: Context
) : ViewModel() {

    private var currentGameId: Int? = null

    var formState by mutableStateOf(GameFormState())
        private set

    private val internalState = mutableStateOf<GameEditUiState>(GameEditUiState.Loading)
    val state: State<GameEditUiState> = internalState

    fun updateForm(newFormState: GameFormState) {
        formState = newFormState
    }

    fun loadGame(gameId: Int) {
        currentGameId = gameId

        viewModelScope.launch {
            internalState.value = GameEditUiState.Loading
            try {
                val game = gamesRepository.getGameById(gameId)
                formState = game.toFormState()
                Log.d(TAG, "Jeu chargé pour édition depuis le back id=$gameId")
                internalState.value = GameEditUiState.Ready
            } catch (e: Exception) {
                Log.e(TAG, "Erreur chargement back, tentative Room", e)

                try {
                    val localGame = gamesRepository.getLocalGameById(gameId)
                    if (localGame != null) {
                        formState = localGame.toFormState()
                        Log.d(TAG, "Jeu chargé pour édition depuis Room id=$gameId")
                        internalState.value = GameEditUiState.Ready
                    } else {
                        internalState.value = GameEditUiState.Error("Impossible de charger le jeu")
                    }
                } catch (localException: Exception) {
                    Log.e(TAG, "Erreur chargement Room", localException)
                    internalState.value = GameEditUiState.Error("Impossible de charger le jeu")
                }
            }
        }
    }

    fun updateGame() {
        val id = currentGameId
        if (id == null) {
            internalState.value = GameEditUiState.Error("Id du jeu introuvable")
            return
        }

        val validationError = GameFormValidator.validate(formState)
        if (validationError != null) {
            internalState.value = GameEditUiState.Error(validationError)
            return
        }
        // On bloque la modification si hors ligne
        if (!NetworkUtils.isInternetAvailable(context)) {
            internalState.value = GameEditUiState.Error("Mode hors ligne : modification impossible")
            return
        }

        viewModelScope.launch {
            internalState.value = GameEditUiState.Saving
            try {
                gamesRepository.updateGame(id, formState.toGameRequest(id))
                Log.d(TAG, "Jeu modifié id=$id")
                internalState.value = GameEditUiState.Success
            } catch (e: Exception) {
                Log.e(TAG, "Erreur modification jeu", e)
                internalState.value =
                    GameEditUiState.Error("Modification impossible : ${e.message ?: "erreur inconnue"}")
            }
        }
    }
}