package com.example.clicker.ui.screens.gameDetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.ui.theme.ButtonBlue
import com.example.clicker.ui.theme.ButtonOrange
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun GameDetailScreen(
    gameId: Int,
    refreshKey: Int,
    onEditClick: (Int) -> Unit,
    onDeleteSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GameDetailViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    LaunchedEffect(gameId, refreshKey) {
        viewModel.loadGame(gameId)
    }

    when (val uiState = viewModel.state.value) {
        is GameDetailUiState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is GameDetailUiState.Error -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is GameDetailUiState.Success -> {
            val game = uiState.game

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Auteur : ${game.author}")
                Text("Type : ${game.type}")
                Text("Joueurs : ${game.nbMinPlayers} à ${game.nbMaxPlayers}")
                Text("Âge minimum : ${game.ageMin}")
                Text("Durée : ${game.duree} min")
                Text("Éditeur ID : ${game.editorId}")
                Text("Prototype : ${if (game.prototype) "Oui" else "Non"}")

                Spacer(modifier = Modifier.height(16.dp))

                Text("Description", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(game.description.ifBlank { "-" })

                Spacer(modifier = Modifier.height(16.dp))

                Text("Notice", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(6.dp))
                Text(game.notice.ifBlank { "-" })

                Spacer(modifier = Modifier.height(16.dp))

                Text("Image URL : ${game.imageUrl.ifBlank { "-" }}")
                Text("Vidéo règles : ${game.videoRulesUrl.ifBlank { "-" }}")

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { onEditClick(game.id) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
                    ) {
                        Text("Modifier")
                    }

                    Button(
                        onClick = { viewModel.deleteGame(onDeleteSuccess) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = ButtonOrange)
                    ) {
                        Text("Supprimer")
                    }
                }
            }
        }
    }
}