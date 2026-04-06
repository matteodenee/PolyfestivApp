package com.example.clicker.ui.screens.festivalEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ZonesPlanScreen(
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
            "Zone Plan A" to listOf(
                "Surface allouée : 20m2",
                "Prix par m2 : 10 €",
                "Places assises : 50"
            ),
            "Zone Plan B" to listOf(
                "Surface allouée : 40m2",
                "Prix par m2 : 8 €",
                "Places assises : 100"
            )
        )

        zones.forEach { (name, details) ->
            ListItemRow(
                title = name.replace("Plan ", ""),
                icon = Icons.Default.Map,
                details = details,
                onEditClick = { onNavigateToEdit(name) }
            )
        }
    }
}
