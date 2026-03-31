package com.example.clicker.ui.screens.reservationSupplies

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.reservation.ReservationDto
import com.example.clicker.data.reservationGame.ReservationGameDto
import com.example.clicker.data.reservationTariffzoneAllocation.ReservationTariffzoneAllocationDto
import com.example.clicker.data.tarifZone.TarifZoneDto
import com.example.clicker.ui.theme.ButtonBlue
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun ReservationSuppliesScreen(
    reservation: ReservationDto,
    modifier: Modifier = Modifier,
    viewModel: ReservationSuppliesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(reservation.id) {
        viewModel.loadSupplies(reservation)
    }

    val state = viewModel.state.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        when (state) {
            is ReservationSuppliesUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is ReservationSuppliesUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is ReservationSuppliesUiState.Success -> {
                val currentReservation = state.reservation

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        Text(
                            text = "Ajout de tables hors jeux",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    item {
                        Text(
                            text = "Choisir la zone tarifaire",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    item {
                        TariffZoneField(
                            options = state.tariffZones,
                            selectedTariffZoneId = viewModel.selectedTariffZoneId,
                            onSelected = viewModel::onTariffZoneChange
                        )
                    }

                    item {
                        SmallNumberField(
                            value = viewModel.tableCountInput,
                            onValueChange = viewModel::onTableCountChange,
                            placeholder = "nombre de tables"
                        )
                    }

                    item {
                        Button(
                            onClick = { viewModel.addAllocation(currentReservation) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Valider")
                        }
                    }

                    if (state.allocations.isNotEmpty()) {
                        items(state.allocations) { allocation ->
                            AllocationRow(
                                allocation = allocation,
                                zoneName = state.tariffZones.firstOrNull { it.id == allocation.tariffzoneId }?.name
                                    ?: "Zone #${allocation.tariffzoneId}",
                                onDelete = {
                                    viewModel.deleteAllocation(
                                        allocationId = allocation.id,
                                        reservation = currentReservation
                                    )
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Jeux de la réservation",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    item {
                        Text(
                            text = "Ajouter un jeu à la réservation :",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    item {
                        GameField(
                            gameNamesById = viewModel.gameNamesById,
                            selectedGameId = viewModel.selectedGameId,
                            onSelected = viewModel::onGameChange
                        )
                    }

                    item {
                        SmallNumberField(
                            value = viewModel.gameTablesInput,
                            onValueChange = viewModel::onGameTablesChange,
                            placeholder = "nombre de tables"
                        )
                    }

                    item {
                        SmallNumberField(
                            value = viewModel.gameChairsInput,
                            onValueChange = viewModel::onGameChairsChange,
                            placeholder = "nombre de chaises"
                        )
                    }

                    item {
                        SmallNumberField(
                            value = viewModel.gameOutletsInput,
                            onValueChange = viewModel::onGameOutletsChange,
                            placeholder = "nombre de prises"
                        )
                    }

                    item {
                        Button(
                            onClick = { viewModel.addReservationGame(currentReservation) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Valider")
                        }
                    }

                    if (state.reservationGames.isNotEmpty()) {
                        item {
                            ReservationGamesTableHeader()
                        }

                        items(state.reservationGames) { reservationGame ->
                            ReservationGameRow(
                                reservationGame = reservationGame,
                                gameName = viewModel.gameNamesById[reservationGame.gameId]
                                    ?: "Jeu #${reservationGame.gameId}",
                                onDelete = {
                                    viewModel.deleteReservationGame(
                                        reservationGameId = reservationGame.id,
                                        reservation = currentReservation
                                    )
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Suivi des jeux",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }

                    item {
                        TrackingRow(
                            label = "L’éditeur présente ses jeux",
                            checked = currentReservation.presentsGames,
                            onCheckedChange = { checked ->
                                viewModel.updateTracking(
                                    reservation = currentReservation,
                                    presentsGames = checked
                                )
                            }
                        )
                    }

                    item {
                        TrackingRow(
                            label = "Liste des jeux demandée",
                            checked = currentReservation.gamesListRequested,
                            onCheckedChange = { checked ->
                                viewModel.updateTracking(
                                    reservation = currentReservation,
                                    gamesListRequested = checked
                                )
                            }
                        )
                    }

                    item {
                        TrackingRow(
                            label = "Liste des jeux obtenue",
                            checked = currentReservation.gamesListReceived,
                            onCheckedChange = { checked ->
                                viewModel.updateTracking(
                                    reservation = currentReservation,
                                    gamesListReceived = checked
                                )
                            }
                        )
                    }

                    item {
                        TrackingRow(
                            label = "Jeux reçus",
                            checked = currentReservation.gamesReceived,
                            onCheckedChange = { checked ->
                                viewModel.updateTracking(
                                    reservation = currentReservation,
                                    gamesReceived = checked
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TariffZoneField(
    options: List<TarifZoneDto>,
    selectedTariffZoneId: Int?,
    onSelected: (Int) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }
    val selectedLabel = options.firstOrNull { it.id == selectedTariffZoneId }?.name ?: ""
    val borderColor =
        if (selectedTariffZoneId != null) ButtonBlue else MaterialTheme.colorScheme.outline

    Box {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded.value = true }
                .border(1.dp, borderColor, RoundedCornerShape(20.dp)),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (selectedLabel.isBlank()) "Zone tarifaire" else selectedLabel,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = ButtonBlue
                )
            }
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            options.forEach { zone ->
                DropdownMenuItem(
                    text = { Text(zone.name) },
                    onClick = {
                        onSelected(zone.id)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Composable
private fun GameField(
    gameNamesById: Map<Int, String>,
    selectedGameId: Int?,
    onSelected: (Int) -> Unit
) {
    val searchText = remember(selectedGameId, gameNamesById) {
        mutableStateOf(selectedGameId?.let { gameNamesById[it] } ?: "")
    }

    val filteredGames = gameNamesById
        .toList()
        .sortedBy { it.second.lowercase() }
        .filter { (_, name) ->
            searchText.value.isNotBlank() &&
                    name.contains(searchText.value, ignoreCase = true)
        }
        .take(6)

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = searchText.value,
            onValueChange = { searchText.value = it },
            placeholder = { Text("Rechercher un jeu") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = ButtonBlue
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = ButtonBlue,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                cursorColor = ButtonBlue
            )
        )

        if (searchText.value.isNotBlank()) {
            if (filteredGames.isEmpty()) {
                Text(
                    text = "Aucun jeu trouvé",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            } else {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                ) {
                    Column {
                        filteredGames.forEachIndexed { index, (id, name) ->
                            val isSelected = id == selectedGameId

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onSelected(id)
                                        searchText.value = name
                                    }
                                    .background(
                                        if (isSelected) ButtonBlue.copy(alpha = 0.12f)
                                        else Color.Transparent
                                    )
                                    .padding(horizontal = 14.dp, vertical = 12.dp)
                            ) {
                                Text(
                                    text = name,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            if (index < filteredGames.lastIndex) {
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ButtonBlue,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            cursorColor = ButtonBlue,
            focusedLabelColor = ButtonBlue
        )
    )
}

@Composable
private fun AllocationRow(
    allocation: ReservationTariffzoneAllocationDto,
    zoneName: String,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("$zoneName — ${allocation.quantityTables} table(s)")
        TextButton(onClick = onDelete) {
            Text(
                text = "Supprimer",
                color = Color.Black
            )
        }
    }
}

@Composable
private fun ReservationGamesTableHeader() {
    Column {
        HorizontalDivider()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Jeu",
                modifier = Modifier.weight(1.8f),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Tables",
                modifier = Modifier.weight(0.9f),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Chaises",
                modifier = Modifier.weight(0.9f),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Prises",
                modifier = Modifier.weight(0.9f),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = "Action",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelMedium
            )
        }
        HorizontalDivider()
    }
}

@Composable
private fun ReservationGameRow(
    reservationGame: ReservationGameDto,
    gameName: String,
    onDelete: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = gameName,
                modifier = Modifier.weight(1.8f)
            )
            Text(
                text = "${reservationGame.tablesNeeded ?: 0}",
                modifier = Modifier.weight(0.9f)
            )
            Text(
                text = "${reservationGame.chairsNeeded ?: 0}",
                modifier = Modifier.weight(0.9f)
            )
            Text(
                text = "${reservationGame.outletsNeeded ?: 0}",
                modifier = Modifier.weight(0.9f)
            )
            TextButton(
                onClick = onDelete,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Supprimer",
                    color = Color.Black
                )
            }
        }
        HorizontalDivider()
    }
}

@Composable
private fun TrackingRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label)

        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkedColor = ButtonBlue,
                checkmarkColor = Color.White,
                uncheckedColor = ButtonBlue.copy(alpha = 0.5f)
            )
        )
    }
}