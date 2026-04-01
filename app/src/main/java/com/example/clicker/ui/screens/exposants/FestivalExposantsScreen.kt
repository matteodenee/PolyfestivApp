package com.example.clicker.ui.screens.exposants

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.exposants.FestivalExposantItem
import com.example.clicker.data.exposants.typeLabel
import com.example.clicker.ui.theme.SearchField
import com.example.clicker.ui.viewmodel.AppViewModelProvider

private enum class FestivalExposantsSortColumn {
    NOM, TYPE, CONTACT, ETAT
}

@Composable
fun FestivalExposantsScreen(
    festivalId: Int,
    festivalName: String?,
    onExposantClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ExposantViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(festivalId) {
        viewModel.loadExposantsByFestival(festivalId)
    }

    val state = viewModel.festivalUiState.collectAsState().value
    val colors = MaterialTheme.colorScheme
    var searchQuery by remember { mutableStateOf("") }
    var sortColumn by remember { mutableStateOf(FestivalExposantsSortColumn.NOM) }
    var ascending by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(20.dp)
    ) {
        Text(
            text = festivalName?.let { "Exposants du festival - $it" } ?: "Exposants du festival",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = colors.onBackground
            )
        )

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
                    text = "Rechercher un exposant",
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
            FestivalExposantsUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colors.primary)
                }
            }

            is FestivalExposantsUiState.Error -> {
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

            is FestivalExposantsUiState.Success -> {
                val query = searchQuery.trim().lowercase()

                val filtered = uiState.exposants.filter { item ->
                    query.isBlank() ||
                            item.exposant.name.lowercase().contains(query) ||
                            item.exposant.typeLabel().lowercase().contains(query) ||
                            festivalContactLabel(item).lowercase().contains(query) ||
                            festivalStatusLabel(item).lowercase().contains(query)
                }

                val sorted = when (sortColumn) {
                    FestivalExposantsSortColumn.NOM ->
                        filtered.sortedBy { it.exposant.name.lowercase() }

                    FestivalExposantsSortColumn.TYPE ->
                        filtered.sortedBy { it.exposant.typeLabel().lowercase() }

                    FestivalExposantsSortColumn.CONTACT ->
                        filtered.sortedBy { festivalContactLabel(it).lowercase() }

                    FestivalExposantsSortColumn.ETAT ->
                        filtered.sortedBy { festivalStatusLabel(it).lowercase() }
                }.let { if (ascending) it else it.reversed() }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .horizontalScroll(rememberScrollState())
                ) {
                    FestivalExposantsHeaderRow(
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
                        FestivalExposantRow(
                            item = item,
                            onClick = { onExposantClick(item.exposant.id) }
                        )
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun FestivalExposantsHeaderRow(
    currentSort: FestivalExposantsSortColumn,
    ascending: Boolean,
    onHeaderClick: (FestivalExposantsSortColumn) -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.surfaceVariant) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FestivalExposantsHeaderCell(
                text = sortLabel("Nom", FestivalExposantsSortColumn.NOM, currentSort, ascending),
                modifier = Modifier.width(170.dp),
                onClick = { onHeaderClick(FestivalExposantsSortColumn.NOM) }
            )
            FestivalExposantsHeaderCell(
                text = sortLabel("Type", FestivalExposantsSortColumn.TYPE, currentSort, ascending),
                modifier = Modifier.width(140.dp),
                onClick = { onHeaderClick(FestivalExposantsSortColumn.TYPE) }
            )
            FestivalExposantsHeaderCell(
                text = sortLabel("Contact", FestivalExposantsSortColumn.CONTACT, currentSort, ascending),
                modifier = Modifier.width(140.dp),
                onClick = { onHeaderClick(FestivalExposantsSortColumn.CONTACT) }
            )
            FestivalExposantsHeaderCell(
                text = sortLabel("Etat", FestivalExposantsSortColumn.ETAT, currentSort, ascending),
                modifier = Modifier.width(140.dp),
                onClick = { onHeaderClick(FestivalExposantsSortColumn.ETAT) }
            )
        }
    }
}

@Composable
private fun FestivalExposantsHeaderCell(
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
private fun FestivalExposantRow(
    item: FestivalExposantItem,
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
            text = item.exposant.name,
            modifier = Modifier.width(170.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = item.exposant.typeLabel(),
            modifier = Modifier.width(140.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = festivalContactLabel(item),
            modifier = Modifier.width(140.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = festivalStatusLabel(item),
            modifier = Modifier.width(140.dp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

private fun festivalContactLabel(item: FestivalExposantItem): String {
    return if (item.contacted) "Contacté" else "Non contacté"
}

private fun festivalStatusLabel(item: FestivalExposantItem): String {
    return item.status
        ?.replace("_", " ")
        ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        ?: "—"
}

private fun sortLabel(
    label: String,
    cell: FestivalExposantsSortColumn,
    current: FestivalExposantsSortColumn,
    ascending: Boolean
): String {
    return if (cell == current) {
        if (ascending) "$label ↑" else "$label ↓"
    } else {
        label
    }
}