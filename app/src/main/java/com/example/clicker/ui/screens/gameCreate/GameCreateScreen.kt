package com.example.clicker.ui.screens.gameCreate

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.ui.utils.gameUtils.GameForm
import com.example.clicker.ui.viewmodel.AppViewModelProvider
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun GameCreateScreen(
    onCreateSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameCreateViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    when (val uiState = viewModel.state.value) {
        is GameCreateUiState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(120.dp))
                CircularProgressIndicator()
            }
        }

        is GameCreateUiState.Success -> {
            LaunchedEffect(Unit) {
                onCreateSuccess()
            }
        }

        else -> {
            GameForm(
                title = "Ajouter un jeu",
                formState = viewModel.formState,
                onFormChange = viewModel::updateForm,
                onSubmit = viewModel::createGame,
                submitLabel = "Enregistrer",
                errorMessage = (uiState as? GameCreateUiState.Error)?.message,
                modifier = modifier
            )
        }
    }
}
