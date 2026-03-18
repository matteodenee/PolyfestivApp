package com.example.clicker.ui.screens.gameEdit

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
import kotlinx.coroutines.launch

class GameEditViewModel(
    private val gamesRepository: GamesRepository
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
                Log.d(TAG, "Jeu chargé pour édition id=$gameId")
                internalState.value = GameEditUiState.Ready
            } catch (e: Exception) {
                Log.e(TAG, "Erreur chargement édition", e)
                internalState.value = GameEditUiState.Error("Impossible de charger le jeu")
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
