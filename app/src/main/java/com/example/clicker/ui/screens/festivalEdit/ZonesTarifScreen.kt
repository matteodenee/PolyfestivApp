package com.example.clicker.ui.screens.festivalEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Euro
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.zone.TariffZoneDto
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun ZonesTarifScreen(
    festivalId: Int,
    onNavigateToEdit: (String) -> Unit,
    viewModel: ZonesTarifViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(festivalId) {
        viewModel.loadZones(festivalId)
    }

    var editingZone by remember { mutableStateOf<TariffZoneDto?>(null) }

    if (editingZone != null) {
        var mutableZone by remember { mutableStateOf(editingZone!!) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9E1))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            EditTextField(
                value = mutableZone.name,
                onValueChange = { mutableZone = mutableZone.copy(name = it) },
                label = "Nom"
            )
            EditTextField(
                value = mutableZone.tableprice.toString(),
                onValueChange = { mutableZone = mutableZone.copy(tableprice = it.toDoubleOrNull() ?: mutableZone.tableprice) },
                label = "Tarif table"
            )
            EditTextField(
                value = mutableZone.pricem2.toString(),
                onValueChange = { mutableZone = mutableZone.copy(pricem2 = it.toDoubleOrNull() ?: mutableZone.pricem2) },
                label = "Prix m2 nu"
            )
            ValiderButton(onClick = {
                viewModel.updateZone(mutableZone)
                editingZone = null
            })
        }
        return
    }

    when (val state = viewModel.uiState.value) {
        is ZonesTarifUiState.Loading -> Text("Chargement...", modifier = Modifier.padding(16.dp))
        is ZonesTarifUiState.Error -> Text(state.message, color = Color.Red, modifier = Modifier.padding(16.dp))
        is ZonesTarifUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF9E1))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                state.zones.forEach { zone ->
                    ListItemRow(
                        title = zone.name,
                        icon = Icons.Default.Euro,
                        details = listOf(
                            "Tarif table: ${zone.tableprice} €",
                            "Prix m2 nu: ${zone.pricem2} €"
                        ),
                        onEditClick = { editingZone = zone }
                    )
                }
            }
        }
    }
}
