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
fun ModifDetailScreen(
    festivalId: Int,
    onBackClick: () -> Unit
) {
    var nom by remember { mutableStateOf("Festival 1") }
    var debut by remember { mutableStateOf("02/01/2026") }
    var fin by remember { mutableStateOf("02/04/2026") }
    var capacite by remember { mutableStateOf("40 tables") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9E1)) // Light yellowish background
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        EditTextField(value = nom, onValueChange = { nom = it }, label = "Nom")
        EditTextField(value = debut, onValueChange = { debut = it }, label = "Début de l'évènement")
        EditTextField(value = fin, onValueChange = { fin = it }, label = "Date de fin")
        EditTextField(value = capacite, onValueChange = { capacite = it }, label = "Capacité")
        
        ValiderButton(onClick = onBackClick)
    }
}
