package com.example.clicker.ui.screens.gameCreate

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
import com.example.clicker.ui.utils.gameUtils.toGameRequest
import kotlinx.coroutines.launch

class GameCreateViewModel(
    private val gamesRepository: GamesRepository
) : ViewModel() {

    var formState by mutableStateOf(GameFormState())
        private set

    private val internalState = mutableStateOf<GameCreateUiState>(GameCreateUiState.Idle)
    val state: State<GameCreateUiState> = internalState

    fun updateForm(newFormState: GameFormState) {
        formState = newFormState
    }

    fun createGame() {
        val validationError = GameFormValidator.validate(formState)
        if (validationError != null) {
            internalState.value = GameCreateUiState.Error(validationError)
            return
        }

        viewModelScope.launch {
            internalState.value = GameCreateUiState.Loading
            try {
                gamesRepository.createGame(formState.toGameRequest())
                Log.d(TAG, "Jeu créé : ${formState.name.trim()}")
                internalState.value = GameCreateUiState.Success
            } catch (e: Exception) {
                Log.e(TAG, "Erreur création jeu", e)
                internalState.value =
                    GameCreateUiState.Error("Création impossible : ${e.message ?: "erreur inconnue"}")
            }
        }
    }
}
