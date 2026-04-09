package com.example.clicker.ui.screens.reservationContact

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.clicker.data.reservationContact.ReservationContactDto
import com.example.clicker.ui.theme.ButtonBlue
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun ReservationContactScreen(
    reservation: ReservationDto,
    modifier: Modifier = Modifier,
    viewModel: ReservationContactViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(reservation.id) {
        viewModel.loadContactData(reservation)
    }

    val uiState = viewModel.state.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        when (val state = uiState) {
            is ReservationContactUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is ReservationContactUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is ReservationContactUiState.Success -> {
                val currentReservation = state.reservation
                val availableStatuses =
                    viewModel.availableNextStatuses(currentReservation.status)

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Changement de statut",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    item {
                        StatusButtonsRow(
                            currentStatus = currentReservation.status,
                            availableStatuses = availableStatuses,
                            onStatusClick = { nextStatus ->
                                viewModel.changeStatus(
                                    reservation = currentReservation,
                                    nextStatus = nextStatus
                                )
                            }
                        )
                    }

                    item {
                        Text(
                            text = "Prise de contact",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    item {
                        OutlinedTextField(
                            value = viewModel.notesInput,
                            onValueChange = viewModel::onNotesChange,
                            placeholder = { Text("Notes de relance...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = false,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ButtonBlue,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                                cursorColor = ButtonBlue
                            )
                        )
                    }

                    item {
                        Button(
                            onClick = {
                                viewModel.addContact(currentReservation)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Valider")
                        }
                    }

                    if (state.contacts.isNotEmpty()) {
                        items(state.contacts) { contact ->
                            ReservationContactItem(contact = contact)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusButtonsRow(
    currentStatus: Int,
    availableStatuses: List<Int>,
    onStatusClick: (Int) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        availableStatuses.forEach { status ->
            val isCurrent = status == currentStatus
            val backgroundColor =
                if (isCurrent) Color(0xFFFF6B4A) else ButtonBlue

            Button(
                onClick = { onStatusClick(status) },
                enabled = !isCurrent,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = backgroundColor,
                    contentColor = Color.White,
                    disabledContainerColor = backgroundColor,
                    disabledContentColor = Color.White
                )
            ) {
                Text(
                    text = when (status) {
                        0 -> "Pas encore contacté"
                        1 -> "Contact pris"
                        2 -> "Discussion"
                        3 -> "Sera absent"
                        4 -> "Considéré absent"
                        5 -> "Confirmé"
                        6 -> "Facturé"
                        7 -> "Payé"
                        else -> "Inconnu"
                    }
                )
            }
        }
    }
}

@Composable
private fun ReservationContactItem(
    contact: ReservationContactDto
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        val date = contact.contactDate ?: "Date inconnue"
        val note = contact.notes ?: ""

        Text(
            text = date,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
        )

        if (note.isNotBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = note,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}