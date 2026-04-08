package com.example.clicker.ui.screens.festivalEdit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.clicker.data.festival.FestivalDto
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun ModifDetailScreen(
    festivalId: Int,
    onBackClick: () -> Unit,
    viewModel: ModifDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(festivalId) {
        viewModel.loadFestival(festivalId)
    }

    when (val state = viewModel.uiState.value) {
        is ModifDetailUiState.Loading -> {
            Text("Chargement...", modifier = Modifier.padding(16.dp))
        }
        is ModifDetailUiState.Error -> {
            Text(state.message, color = Color.Red, modifier = Modifier.padding(16.dp))
        }
        is ModifDetailUiState.Success -> {
            var festival by remember { mutableStateOf(state.festival) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFFF9E1)) // Light yellowish background
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                EditTextField(value = festival.name, onValueChange = { festival = festival.copy(name = it) }, label = "Nom")
                EditTextField(value = festival.startDate, onValueChange = { festival = festival.copy(startDate = it) }, label = "Début de l'évènement")
                EditTextField(value = festival.endDate, onValueChange = { festival = festival.copy(endDate = it) }, label = "Date de fin")
                EditTextField(value = festival.description, onValueChange = { festival = festival.copy(description = it) }, label = "Description")
                
                ValiderButton(onClick = {
                    viewModel.updateFestival(festival)
                    onBackClick() // Optimistic UI: navigate back
                })
            }
        }
    }
}
