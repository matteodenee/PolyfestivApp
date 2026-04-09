package com.example.clicker.ui.screens.festivalEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chair
import androidx.compose.material.icons.filled.ElectricalServices
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.equipment.EquipmentDto
import com.example.clicker.data.equipment.EquipmentRequest
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun StockMaterielScreen(
    festivalId: Int,
    onNavigateToEdit: (String) -> Unit,
    viewModel: StockMaterielViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(festivalId) {
        viewModel.loadEquipments(festivalId)
    }

    var editingEq by remember { mutableStateOf<EquipmentDto?>(null) }
    var isCreatingEq by remember { mutableStateOf(false) }

    if (isCreatingEq) {
        var kindStr by remember { mutableStateOf("") }
        var quantityStr by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9E1))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Nouvel Equipement")
            EnumDropdownMenu(
                selectedValue = kindStr,
                options = listOf("CHAIR", "ELECTRIC_OUTLET"),
                label = "Type",
                onValueChangedEvent = { kindStr = it }
            )
            EditTextField(
                value = quantityStr,
                onValueChange = { quantityStr = it },
                label = "Quantité"
            )
            ValiderButton(onClick = {
                val finalKind = kindStr.ifBlank { "CHAIR" }
                val newQty = quantityStr.toIntOrNull() ?: 0
                viewModel.addEquipment(EquipmentRequest(festivalId, finalKind, 0.0, newQty))
                isCreatingEq = false
            })
            TextButton(
                onClick = { isCreatingEq = false },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Annuler")
            }
        }
        return
    }

    if (editingEq != null) {
        var quantityStr by remember { mutableStateOf(editingEq!!.quantity.toString()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9E1))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Equipement: ${editingEq!!.kind}")
            EditTextField(
                value = quantityStr,
                onValueChange = { quantityStr = it },
                label = "Quantité"
            )
            ValiderButton(onClick = {
                val newQty = quantityStr.toIntOrNull() ?: editingEq!!.quantity
                viewModel.updateEquipment(editingEq!!.copy(quantity = newQty))
                editingEq = null
            })
        }
        return
    }

    when (val state = viewModel.uiState.value) {
        is StockMaterielUiState.Loading -> Text("Chargement...", modifier = Modifier.padding(16.dp))
        is StockMaterielUiState.Error -> Text(state.message, color = Color.Red, modifier = Modifier.padding(16.dp))
        is StockMaterielUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF9E1))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                state.equipments.forEach { eq ->
                    val icon = if (eq.kind.contains("CHAIR", true)) Icons.Default.Chair else Icons.Default.ElectricalServices
                    ListItemRow(
                        title = eq.kind,
                        icon = icon,
                        details = listOf("Quantité: ${eq.quantity}"),
                        onEditClick = { editingEq = eq }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = { isCreatingEq = true },
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 32.dp)
                ) {
                    Text("Ajouter du matériel")
                }
            }
        }
    }
}
