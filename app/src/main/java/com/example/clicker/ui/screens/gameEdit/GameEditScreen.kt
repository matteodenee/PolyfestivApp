package com.example.clicker.ui.screens.gameEdit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.ui.utils.gameUtils.GameForm
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun GameEditScreen(
    gameId: Int,
    onEditSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameEditViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    LaunchedEffect(gameId) {
        viewModel.loadGame(gameId)
    }

    when (val uiState = viewModel.state.value) {
        is GameEditUiState.Loading,
        is GameEditUiState.Saving -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(120.dp))
                CircularProgressIndicator()
            }
        }

        is GameEditUiState.Success -> {
            LaunchedEffect(Unit) {
                onEditSuccess()
            }
        }

        else -> {
            GameForm(
                title = "Modifier le jeu",
                formState = viewModel.formState,
                onFormChange = viewModel::updateForm,
                onSubmit = viewModel::updateGame,
                submitLabel = "Enregistrer",
                errorMessage = (uiState as? GameEditUiState.Error)?.message,
                modifier = modifier
            )
        }
    }
}
