package com.example.clicker.ui.screens.festivalGames

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.game.FestivalGameItem
import com.example.clicker.ui.theme.SearchField
import com.example.clicker.ui.viewmodel.AppViewModelProvider

private enum class FestivalGamesSortColumn {
    JEU, AUTEUR, EDITEUR
}

@Composable
fun FestivalGamesScreen(
    festivalId: Int,
    festivalName: String?,
    onGameClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FestivalGamesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(festivalId) {
        viewModel.loadGamesByFestival(festivalId)
    }

    val state by viewModel.uiState.collectAsState()
    val colors = MaterialTheme.colorScheme
    var searchQuery by remember { mutableStateOf("") }
    var sortColumn by remember { mutableStateOf(FestivalGamesSortColumn.JEU) }
    var ascending by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(20.dp)
    ) {
        Text(
            text = festivalName?.let { "Jeux du festival - $it" } ?: "Jeux du festival",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = colors.onBackground
            )
        )

        Spacer(modifier = Modifier.width(1.dp))

        Text(
            text = "Festival ID : $festivalId",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Rechercher un jeu",
                    color = colors.onSurface.copy(alpha = 0.7f)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Recherche"
                )
            },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = SearchField,
                unfocusedContainerColor = SearchField,
                focusedIndicatorColor = SearchField,
                unfocusedIndicatorColor = SearchField
            )
        )

        when (val uiState = state) {
            FestivalGamesUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }

            is FestivalGamesUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        color = colors.error
                    )
                }
            }

            is FestivalGamesUiState.Success -> {
                val query = searchQuery.trim().lowercase()

                val filtered = uiState.games.filter { item ->
                    query.isBlank() ||
                        item.game.name.lowercase().contains(query) ||
                        item.game.author.lowercase().contains(query) ||
                        (item.editorName ?: "").lowercase().contains(query)
                }

                val sorted = when (sortColumn) {
                    FestivalGamesSortColumn.JEU ->
                        filtered.sortedBy { it.game.name.lowercase() }

                    FestivalGamesSortColumn.AUTEUR ->
                        filtered.sortedBy { it.game.author.lowercase() }

                    FestivalGamesSortColumn.EDITEUR ->
                        filtered.sortedBy { (it.editorName ?: "").lowercase() }
                }.let { if (ascending) it else it.reversed() }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    FestivalGamesHeaderRow(
                        currentSort = sortColumn,
                        ascending = ascending,
                        onHeaderClick = { clicked ->
                            if (clicked == sortColumn) {
                                ascending = !ascending
                            } else {
                                sortColumn = clicked
                                ascending = true
                            }
                        }
                    )

                    HorizontalDivider()

                    sorted.forEach { item ->
                        FestivalGameRow(
                            item = item,
                            onClick = { onGameClick(item.game.id) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun FestivalGamesHeaderRow(
    currentSort: FestivalGamesSortColumn,
    ascending: Boolean,
    onHeaderClick: (FestivalGamesSortColumn) -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FestivalGamesHeaderCell(
                text = sortLabel("Jeu", FestivalGamesSortColumn.JEU, currentSort, ascending),
                modifier = Modifier.width(180.dp),
                onClick = { onHeaderClick(FestivalGamesSortColumn.JEU) }
            )
            FestivalGamesHeaderCell(
                text = sortLabel("Auteur", FestivalGamesSortColumn.AUTEUR, currentSort, ascending),
                modifier = Modifier.width(160.dp),
                onClick = { onHeaderClick(FestivalGamesSortColumn.AUTEUR) }
            )
            FestivalGamesHeaderCell(
                text = sortLabel("Éditeur", FestivalGamesSortColumn.EDITEUR, currentSort, ascending),
                modifier = Modifier.width(180.dp),
                onClick = { onHeaderClick(FestivalGamesSortColumn.EDITEUR) }
            )
        }
    }
}

@Composable
private fun FestivalGamesHeaderCell(
    text: String,
    modifier: Modifier,
    onClick: () -> Unit
) {
    Text(
        text = text,
        modifier = modifier.clickable(onClick = onClick),
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun FestivalGameRow(
    item: FestivalGameItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = item.game.name,
            modifier = Modifier.width(180.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = item.game.author,
            modifier = Modifier.width(160.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = item.editorName ?: "—",
            modifier = Modifier.width(180.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

private fun sortLabel(
    label: String,
    cell: FestivalGamesSortColumn,
    current: FestivalGamesSortColumn,
    ascending: Boolean
): String {
    return if (cell == current) {
        if (ascending) "$label ↑" else "$label ↓"
    } else {
        label
    }
}