package com.example.clicker.ui.screens.exposants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.model.Exposant
import com.example.clicker.ui.theme.AccentBlue
import com.example.clicker.ui.theme.BackgroundCream
import com.example.clicker.ui.theme.BorderLilac

@Composable
fun ExposantsScreen(
    modifier: Modifier = Modifier,
    exposantViewModel: ExposantViewModel = viewModel()
) {
    val uiState by exposantViewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        uiState.errorMessage != null -> {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(text = uiState.errorMessage ?: "Erreur inconnue")
            }
        }

        else -> {
            when (uiState.currentPage) {
                ExposantsPage.LIST -> {
                    ExposantsListContent(
                        searchQuery = uiState.searchQuery,
                        exposantsCount = uiState.filteredExposants.size,
                        exposants = uiState.filteredExposants,
                        onSearchQueryChange = exposantViewModel::onSearchQueryChange,
                        onAddClick = exposantViewModel::openCreateForm,
                        onDetailsClick = exposantViewModel::openDetails,
                        modifier = modifier
                    )
                }

                ExposantsPage.DETAIL -> {
                    uiState.selectedExposant?.let { exposant ->
                        DetailsExposantScreen(
                            exposant = exposant,
                            onBackClick = exposantViewModel::back,
                            onEditClick = exposantViewModel::openEditForm,
                            onDeleteClick = exposantViewModel::deleteSelectedExposant,
                            modifier = modifier
                        )
                    }
                }

                ExposantsPage.FORM -> {
                    ExposantFormScreen(
                        mode = uiState.formMode,
                        exposant = uiState.selectedExposant,
                        onBackClick = exposantViewModel::back,
                        onSaveClick = exposantViewModel::saveExposant,
                        modifier = modifier
                    )
                }
            }
        }
    }
}

@Composable
private fun ExposantsListContent(
    searchQuery: String,
    exposantsCount: Int,
    exposants: List<Exposant>,
    onSearchQueryChange: (String) -> Unit,
    onAddClick: () -> Unit,
    onDetailsClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    var showAll by remember { mutableStateOf(false) }

    val displayedExposants = when {
        searchQuery.isNotBlank() -> exposants
        showAll -> exposants
        else -> exposants.take(10)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundCream)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onAddClick,
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
            contentPadding = PaddingValues(vertical = 14.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Ajouter un exposant")
        }

        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            placeholder = {
                Text("Rechercher")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AccentBlue,
                unfocusedBorderColor = BorderLilac
            ),
            singleLine = true
        )

        Text(
            text = "Tous les acteurs ($exposantsCount)",
            style = MaterialTheme.typography.titleLarge
        )

        ListeExposants(
            exposants = displayedExposants,
            onDetailsClick = onDetailsClick,
            modifier = Modifier.fillMaxWidth()
        )

        if (searchQuery.isBlank() && !showAll && exposants.size > 10) {
            TextButton(
                onClick = { showAll = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("View All Actors")
            }
        }
    }
}