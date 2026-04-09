package com.example.clicker.ui.screens.publicPlan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun PublicPlanScreen(
    festivalId: Int,
    modifier: Modifier = Modifier,
    viewModel: PublicPlanViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(festivalId) {
        viewModel.loadInitial(festivalId)
    }

    val uiState = viewModel.state.value

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        when (val state = uiState) {
            is PublicPlanUiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is PublicPlanUiState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is PublicPlanUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {

                    item {
                        FestivalSelector(
                            festivals = state.festivals,
                            selectedFestivalId = state.currentFestivalId,
                            onFestivalSelected = viewModel::selectFestival
                        )
                    }

                    item {
                        Text(
                            text = "Zones du plan",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    if (state.zones.isEmpty()) {
                        item {
                            Text("Aucune zone définie pour ce festival.")
                        }
                    } else {
                        items(state.zones) { zone ->
                            ZonePublicCard(zone)
                        }
                    }

                    item {
                        Text(
                            text = "Jeux non placés",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    if (state.unplacedGames.isEmpty()) {
                        item {
                            Text("Aucun jeu non placé.")
                        }
                    } else {
                        items(state.unplacedGames) { placement ->
                            PublicPlacementCard(placement)
                        }
                    }

                    item {
                        Text(
                            text = "Exposants",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    if (state.publishers.isEmpty()) {
                        item {
                            Text("Aucun exposant présent.")
                        }
                    } else {
                        items(state.publishers) { publisher ->
                            PublisherCard(publisher)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FestivalSelector(
    festivals: List<com.example.clicker.data.festival.FestivalDto>,
    selectedFestivalId: Int,
    onFestivalSelected: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedFestivalName = festivals.firstOrNull { it.id == selectedFestivalId }?.name.orEmpty()

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selectedFestivalName,
            onValueChange = {},
            readOnly = true,
            label = { Text("Festival") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            festivals.forEach { festival ->
                DropdownMenuItem(
                    text = { Text(festival.name) },
                    onClick = {
                        expanded = false
                        onFestivalSelected(festival.id)
                    }
                )
            }
        }
    }
}

@Composable
private fun ZonePublicCard(zone: PublicZoneUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = zone.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(zone.tariffzoneLabel, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                "Capacité : ${zone.capacityTables} tables • ${zone.surface} m²",
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(10.dp))

            if (zone.placements.isEmpty()) {
                Text("Aucun jeu placé dans cette zone.", style = MaterialTheme.typography.bodyMedium)
            } else {
                zone.placements.forEachIndexed { index, placement ->
                    if (index > 0) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))
                    }
                    PublicPlacementCard(placement, embedded = true)
                }
            }
        }
    }
}

@Composable
private fun PublicPlacementCard(
    placement: PublicPlacementUi,
    embedded: Boolean = false
) {
    val content: @Composable () -> Unit = {
        Column {
            Text(
                text = placement.gameName,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = placement.reservantName,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "${placement.tablesAllocated} table(s) • ${placement.tableType} • ${placement.chairsAllocated} chaise(s) • ${placement.outletsAllocated} prise(s)",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }

    if (embedded) {
        content()
    } else {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
private fun PublisherCard(publisher: PublicPublisherUi) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = publisher.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            if (publisher.actorTypes.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = publisher.actorTypes.joinToString(", "),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = publisher.games.joinToString(", "),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}