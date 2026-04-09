package com.example.clicker.ui.screens.reservationCreate

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.exposants.Exposant
import com.example.clicker.data.exposants.typeLabel
import com.example.clicker.ui.theme.AccentBlue
import com.example.clicker.ui.theme.ButtonBlue
import com.example.clicker.ui.theme.SearchField
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun ReservationCreateScreen(
    festivalId: Int,
    onCreateSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReservationCreateViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(festivalId) {
        viewModel.loadData(festivalId)
    }

    when (val uiState = viewModel.state.value) {
        is ReservationCreateUiState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(120.dp))
                CircularProgressIndicator()
            }
        }

        is ReservationCreateUiState.Error,
        is ReservationCreateUiState.Success -> {
            val displayedExposants = if (viewModel.searchQuery.isBlank()) {
                viewModel.exposants
            } else {
                viewModel.filteredExposants()
            }

            ReservationCreateContent(
                festivalName = viewModel.festivalName,
                searchQuery = viewModel.searchQuery,
                displayedExposants = displayedExposants,
                selectedExposant = viewModel.selectedExposant,
                errorMessage = (uiState as? ReservationCreateUiState.Error)?.message,
                onSearchChange = viewModel::updateSearchQuery,
                onExposantClick = viewModel::selectExposant,
                onSubmit = {
                    viewModel.createReservation(
                        festivalId = festivalId,
                        onCreated = onCreateSuccess
                    )
                },
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ReservationCreateContent(
    festivalName: String,
    searchQuery: String,
    displayedExposants: List<Exposant>,
    selectedExposant: Exposant?,
    errorMessage: String?,
    onSearchChange: (String) -> Unit,
    onExposantClick: (Exposant) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = festivalName,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = colors.onBackground
            )
        }

        item {
            Text(
                text = "Création d’une nouvelle réservation",
                style = MaterialTheme.typography.titleMedium,
                color = colors.onBackground
            )
        }

        item {
            Text(
                text = "Sélectionnez l’exposant associé à la réservation.",
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onBackground.copy(alpha = 0.7f)
            )
        }

        item {
            Spacer(modifier = Modifier.height(4.dp))
        }

        item {
            ReservationCreateSearchField(
                value = searchQuery,
                onValueChange = onSearchChange
            )
        }

        item {
            Text(
                text = if (searchQuery.isBlank()) {
                    "Liste des exposants"
                } else {
                    "Résultats de recherche"
                },
                style = MaterialTheme.typography.titleSmall,
                color = colors.onBackground,
                fontWeight = FontWeight.Medium
            )
        }

        if (displayedExposants.isEmpty()) {
            item {
                Text(
                    text = if (searchQuery.isBlank()) {
                        "Aucun exposant disponible."
                    } else {
                        "Aucun exposant trouvé pour cette recherche."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.onBackground.copy(alpha = 0.7f)
                )
            }
        } else {
            items(displayedExposants, key = { it.id }) { exposant ->
                ExposantChoiceItem(
                    exposant = exposant,
                    isSelected = selectedExposant?.id == exposant.id,
                    onClick = { onExposantClick(exposant) }
                )
            }
        }

        if (errorMessage != null) {
            item {
                Text(
                    text = errorMessage,
                    color = colors.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Button(
                onClick = onSubmit,
                enabled = selectedExposant != null,
                shape = RoundedCornerShape(18.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Créer la réservation")
            }
        }
    }
}

@Composable
private fun ReservationCreateSearchField(
    value: String,
    onValueChange: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(27.dp)),
        placeholder = {
            Text(
                text = "Rechercher un exposant",
                color = colors.onSurface.copy(alpha = 0.7f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Recherche",
                tint = colors.onSurface
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(27.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SearchField,
            unfocusedContainerColor = SearchField,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = colors.onSurface
        )
    )
}

@Composable
private fun ExposantChoiceItem(
    exposant: Exposant,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = if (isSelected) AccentBlue.copy(alpha = 0.12f) else Color.Transparent
        ) {
            Column(
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Text(
                    text = exposant.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = colors.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = exposant.typeLabel(),
                    style = MaterialTheme.typography.bodySmall,
                    color = colors.onBackground.copy(alpha = 0.7f)
                )

                exposant.email?.takeIf { it.isNotBlank() }?.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = colors.onBackground.copy(alpha = 0.7f)
                    )
                }
            }
        }

        HorizontalDivider(
            color = colors.outline.copy(alpha = 0.15f)
        )
    }
}