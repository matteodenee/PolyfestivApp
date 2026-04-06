package com.example.clicker.ui.screens.festivalEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TableRestaurant
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
import com.example.clicker.data.table.TableDto
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun StockTablesScreen(
    festivalId: Int,
    onNavigateToEdit: (String) -> Unit, // Keeping placeholder just to not break NavHost yet
    viewModel: StockTablesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(festivalId) {
        viewModel.loadTables(festivalId)
    }

    var editingTable by remember { mutableStateOf<TableDto?>(null) }

    if (editingTable != null) {
        var quantityStr by remember { mutableStateOf(editingTable!!.quantity.toString()) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFFF9E1))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text("Table: ${editingTable!!.type}")
            EditTextField(
                value = quantityStr,
                onValueChange = { quantityStr = it },
                label = "Quantité"
            )
            ValiderButton(onClick = {
                val newQty = quantityStr.toIntOrNull() ?: editingTable!!.quantity
                viewModel.updateTable(editingTable!!.copy(quantity = newQty))
                editingTable = null
            })
        }
        return
    }

    when (val state = viewModel.uiState.value) {
        is StockTablesUiState.Loading -> Text("Chargement...", modifier = Modifier.padding(16.dp))
        is StockTablesUiState.Error -> Text(state.message, color = Color.Red, modifier = Modifier.padding(16.dp))
        is StockTablesUiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF9E1))
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                state.tables.forEach { table ->
                    ListItemRow(
                        title = "Table ${table.type}",
                        icon = Icons.Default.TableRestaurant,
                        details = listOf("Quantité: ${table.quantity}"),
                        onEditClick = { editingTable = table }
                    )
                }
            }
        }
    }
}
