package com.example.clicker.ui.screens.reservationPlacement

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
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.ui.theme.ButtonBlue
import com.example.clicker.ui.theme.ButtonGreen
import com.example.clicker.ui.theme.ButtonOrange
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun ReservationPlacementScreen(
    festivalId: Int,
    modifier: Modifier = Modifier,
    viewModel: ReservationPlacementViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(festivalId) {
        viewModel.loadPlacementData(festivalId)
    }

    val uiState = viewModel.state.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        when (val state = uiState) {
            is ReservationPlacementUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is ReservationPlacementUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center),
                    fontSize = 13.sp
                )
            }

            is ReservationPlacementUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    viewModel.actionErrorMessage?.let { message ->
                        item {
                            Text(
                                text = message,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 13.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }

                    item {
                        Text(
                            text = "Résumé",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    item {
                        SummaryBlock(state.summary)
                    }

                    item {
                        Text(
                            text = "Zones du plan",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    items(state.zones) { zone ->
                        ZoneCard(
                            zone = zone,
                            isSelected = viewModel.selectedMapZoneId == zone.mapZone.id,
                            onSelect = { viewModel.selectMapZone(zone.mapZone.id) },
                            onRemovePlacement = { placementId ->
                                viewModel.removePlacement(festivalId, placementId)
                            }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Jeux non placés",
                            style = MaterialTheme.typography.titleLarge,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (state.unplacedGames.isEmpty()) {
                        item {
                            Text(
                                text = "Tous les jeux sont placés.",
                                fontSize = 13.sp,
                                lineHeight = 17.sp
                            )
                        }
                    } else {
                        items(state.unplacedGames) { game ->
                            UnplacedGameCard(
                                game = game,
                                selectedZoneName = state.zones.firstOrNull {
                                    it.mapZone.id == viewModel.selectedMapZoneId
                                }?.mapZone?.name,
                                tableTypes = state.availableTableTypes,
                                selectedTableType = viewModel.selectedTableTypeByReservationGameKey[
                                    "${game.reservationId}-${game.reservationGame.gameId}"
                                ],
                                onTableTypeChange = { type ->
                                    viewModel.onTableTypeChange(
                                        reservationId = game.reservationId,
                                        gameId = game.reservationGame.gameId,
                                        tableType = type
                                    )
                                },
                                onPlace = {
                                    viewModel.placeGame(festivalId, game)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SummaryBlock(summary: PlacementSummaryUi) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Jeux total : ${summary.totalGames}", fontSize = 13.sp, lineHeight = 17.sp)
        Text("Jeux placés : ${summary.placedGames}", fontSize = 13.sp, lineHeight = 17.sp)
        Text("En attente : ${summary.waitingGames}", fontSize = 13.sp, lineHeight = 17.sp)
        Text("Tables utilisées : ${summary.tablesUsed}", fontSize = 13.sp, lineHeight = 17.sp)
        Text(
            "Tables restantes / stock festival : ${summary.tablesRemaining}/${summary.tablesCapacity}",
            fontSize = 13.sp,
            lineHeight = 17.sp
        )
    }
}

@Composable
private fun ZoneCard(
    zone: PlacementZoneUi,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onRemovePlacement: (Int) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = zone.mapZone.name,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Zone tarifaire : ${zone.tariffZoneName}",
                fontSize = 13.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (zone.placedGames.isEmpty()) {
                Text(
                    text = "Aucun jeu placé dans cette zone.",
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
                )
            } else {
                zone.placedGames.forEach { placedGame ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(
                            modifier = Modifier.weight(100f)
                        ) {
                            Text(
                                text = placedGame.gameName,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Medium
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = placedGame.tablesText,
                                fontSize = 12.sp,
                                lineHeight = 16.sp
                            )
                        }

                        Spacer(modifier = Modifier.weight(0.3f))

                        Button(
                            onClick = { onRemovePlacement(placedGame.placement.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonOrange,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ),
                            shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .align(Alignment.Top)
                                .sizeIn(minWidth = 92.dp)
                        ) {
                            Text(
                                text = "Retirer",
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column(
            modifier = Modifier.width(155.dp),
            horizontalAlignment = Alignment.End
        ) {
            Button(
                onClick = onSelect,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonGreen,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isSelected) "Sélectionnée" else "Sélectionner",
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}
@Composable
private fun UnplacedGameCard(
    game: UnplacedGameUi,
    selectedZoneName: String?,
    tableTypes: List<String>,
    selectedTableType: String?,
    onTableTypeChange: (String) -> Unit,
    onPlace: () -> Unit
) {
    val canPlace = !selectedZoneName.isNullOrBlank() && !selectedTableType.isNullOrBlank()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = game.gameName,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onPlace,
                enabled = canPlace,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonBlue,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = ButtonBlue.copy(alpha = 0.45f),
                    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
                ),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.width(90.dp)
            ) {
                Text("Placer", fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Réservation #${game.reservationId}",
            fontSize = 12.sp,
            lineHeight = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${game.tablesNeeded} table(s) • ${game.chairsNeeded} chaise(s) • ${game.outletsNeeded} prise(s)",
            fontSize = 12.sp,
            lineHeight = 16.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (!selectedZoneName.isNullOrBlank()) {
            Text(
                text = "Zone sélectionnée : $selectedZoneName",
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        TableTypeField(
            options = tableTypes,
            selectedValue = selectedTableType,
            onSelected = onTableTypeChange
        )

        Spacer(modifier = Modifier.height(12.dp))
        HorizontalDivider()
    }
}

@Composable
private fun TableTypeField(
    options: List<String>,
    selectedValue: String?,
    onSelected: (String) -> Unit
) {
    val expanded = remember { mutableStateOf(false) }

    Box {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded.value = true }
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(20.dp)
                ),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedValue ?: "Choisir un type de table",
                    modifier = Modifier.weight(1f),
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "▼",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            options.distinct().forEach { type ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = type,
                            fontSize = 12.sp
                        )
                    },
                    onClick = {
                        onSelected(type)
                        expanded.value = false
                    }
                )
            }
        }
    }
}