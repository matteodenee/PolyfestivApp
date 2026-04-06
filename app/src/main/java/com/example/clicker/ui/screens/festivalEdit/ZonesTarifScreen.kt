package com.example.clicker.ui.screens.festivalEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Euro
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ZonesTarifScreen(
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
        val zones = listOf(
            "Zone A" to listOf(
                "Tarif étudiant : 10.00 €",
                "Tarif normal : 12.00 €",
                "Tarif VIP : 40.00 €",
                "Prix m2 nu : 10.00 €"
            ),
            "Zone B" to listOf(
                "Tarif étudiant : 8.00 €",
                "Tarif normal : 10.00 €",
                "Tarif VIP : 30.00 €",
                "Prix m2 nu : 8.00 €"
            )
        )

        zones.forEach { (name, details) ->
            ListItemRow(
                title = name,
                icon = Icons.Default.Euro,
                details = details,
                onEditClick = { onNavigateToEdit(name) }
            )
        }
    }
}
