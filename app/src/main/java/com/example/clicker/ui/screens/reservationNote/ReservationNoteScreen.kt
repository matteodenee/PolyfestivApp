package com.example.clicker.ui.screens.reservationNote

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.reservation.ReservationDto
import com.example.clicker.data.reservationNote.ReservationNoteDto
import com.example.clicker.ui.theme.ButtonBlue
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun ReservationNoteScreen(
    reservation: ReservationDto,
    modifier: Modifier = Modifier,
    viewModel: ReservationNoteViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(reservation.id) {
        viewModel.loadNotes(reservation.id)
    }

    val uiState = viewModel.state.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        when (val state = uiState) {
            is ReservationNoteUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is ReservationNoteUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is ReservationNoteUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Notes sur la réservation",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = viewModel.noteInput,
                            onValueChange = viewModel::onNoteChange,
                            placeholder = { Text("...") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(4.dp),
                            singleLine = false,
                            trailingIcon = {
                                if (viewModel.noteInput.isNotBlank()) {
                                    IconButton(
                                        onClick = { viewModel.onNoteChange("") }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Cancel,
                                            contentDescription = "Effacer"
                                        )
                                    }
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.outline,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                cursorColor = ButtonBlue
                            )
                        )
                    }

                    item {
                        Button(
                            onClick = { viewModel.addNote(reservation) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Valider")
                        }
                    }

                    if (state.notes.isNotEmpty()) {
                        items(state.notes) { note ->
                            ReservationNoteItem(note = note)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReservationNoteItem(
    note: ReservationNoteDto
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = note.content,
            style = MaterialTheme.typography.bodyMedium
        )

        note.author?.takeIf { it.isNotBlank() }?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
        }

        note.createdAt?.takeIf { it.isNotBlank() }?.let {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.55f)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
    }
}