package com.example.clicker.ui.screens.festivalEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TableRestaurant
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StockTablesScreen(
    festivalId: Int,
    onNavigateToEdit: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9E1))
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val tables = listOf(
            "Table de 2" to listOf("Quantité: 10"),
            "Table de 4" to listOf("Quantité: 20"),
            "Table ronde" to listOf("Quantité: 5")
        )

        tables.forEach { (name, details) ->
            ListItemRow(
                title = name,
                icon = Icons.Default.TableRestaurant,
                details = details,
                onEditClick = { onNavigateToEdit(name) }
            )
        }
    }
}
