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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun StockMaterielScreen(
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
        ListItemRow(
            title = "Chaises",
            icon = Icons.Default.Chair,
            details = listOf("Quantité: 150"),
            onEditClick = { onNavigateToEdit("Chaises") }
        )
        ListItemRow(
            title = "Multiprises",
            icon = Icons.Default.ElectricalServices,
            details = listOf("Quantité: 20"),
            onEditClick = { onNavigateToEdit("Multiprises") }
        )
    }
}
