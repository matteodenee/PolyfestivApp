package com.example.clicker.ui.screens.festivalEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GenericEditScreen(
    screenTitle: String,
    onBackClick: () -> Unit
) {
    // We will derive fields based on the screen title just to mock the UI
    val fields = remember(screenTitle) {
        when {
            screenTitle.startsWith("Table") -> listOf("Quantité" to "10")
            screenTitle == "Chaises" || screenTitle == "Multiprises" -> listOf("Quantité disponible" to "50")
            screenTitle.startsWith("Zone") && screenTitle.contains("Plan") -> listOf(
                "Nom" to "Zone A",
                "Surface allouée (m2)" to "20",
                "Prix par m2 (€)" to "10",
                "Places assises" to "50"
            )
            screenTitle.startsWith("Zone") -> listOf( // Zones Tarifaires
                "Nom" to "Zone A",
                "Tarif étudiant" to "10.00",
                "Tarif normal" to "12.00",
                "Tarif VIP" to "40.00",
                "Prix m2 nu" to "10.00"
            )
            else -> listOf("Valeur" to "")
        }
    }

    var textValues by remember { mutableStateOf(fields.map { it.second }) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9E1)) // Light yellowish background
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        fields.forEachIndexed { index, field ->
            EditTextField(
                value = textValues[index],
                onValueChange = { newValue ->
                    val newList = textValues.toMutableList()
                    newList[index] = newValue
                    textValues = newList
                },
                label = field.first
            )
        }

        ValiderButton(onClick = onBackClick)
    }
}
