package com.example.clicker.ui.screens.festivalList

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.example.clicker.ui.screens.festivalEdit.EditTextField
import com.example.clicker.ui.screens.festivalEdit.ValiderButton
import com.example.clicker.ui.viewmodel.AppViewModelProvider
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FestivalCreateScreen(
    onCreateSuccess: (Int) -> Unit,
    viewModel: FestivalCreateViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state = viewModel.uiState.value

    LaunchedEffect(state) {
        if (state is FestivalCreateUiState.Success) {
            onCreateSuccess(state.festivalId)
        }
    }

    val today = remember { LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) }
    
    var name by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf(today) }
    var endDate by remember { mutableStateOf(today) }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9E1)) // Using the same light yellowish background tone
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        
        if (state is FestivalCreateUiState.Error) {
            Text(state.message, color = Color.Red, modifier = Modifier.padding(bottom = 16.dp))
        }

        EditTextField(value = name, onValueChange = { name = it }, label = "Nom")
        EditTextField(value = startDate, onValueChange = { startDate = it }, label = "Début de l'évènement (YYYY-MM-DD)")
        EditTextField(value = endDate, onValueChange = { endDate = it }, label = "Date de fin (YYYY-MM-DD)")
        EditTextField(value = description, onValueChange = { description = it }, label = "Description")

        // Add padding bottom if needed
        ValiderButton(onClick = {
            if(name.isNotBlank()) {
                viewModel.createFestival(name, description, startDate, endDate)
            }
        })
    }
}
