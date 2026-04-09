package com.example.clicker.ui.screens.exposants

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.clicker.data.exposants.Exposant
import com.example.clicker.data.exposants.ExposantFormData
import com.example.clicker.data.exposants.availableActorRoles
import com.example.clicker.data.exposants.toFormData
import com.example.clicker.ui.theme.ButtonBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposantFormScreen(
    mode: ExposantFormMode,
    exposant: Exposant?,
    onBackClick: () -> Unit,
    onSaveClick: (ExposantFormData) -> Unit,
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    val initialData = if (mode == ExposantFormMode.EDIT && exposant != null) {
        exposant.toFormData()
    } else {
        ExposantFormData()
    }

    var name by remember { mutableStateOf(initialData.name) }
    var role by remember { mutableStateOf(initialData.role) }
    var phone by remember { mutableStateOf(initialData.phone) }
    var email by remember { mutableStateOf(initialData.email) }
    var description by remember { mutableStateOf(initialData.description) }

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(mode, exposant) {
        val data = if (mode == ExposantFormMode.EDIT && exposant != null) {
            exposant.toFormData()
        } else {
            ExposantFormData()
        }

        name = data.name
        role = data.role
        phone = data.phone
        email = data.email
        description = data.description
    }

    val title = if (mode == ExposantFormMode.CREATE) {
        "Ajouter un exposant"
    } else {
        "Modifier l'exposant"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nom *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = role,
                onValueChange = {},
                readOnly = true,
                label = { Text("Rôle *") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableActorRoles.forEach { actorRole ->
                    DropdownMenuItem(
                        text = { Text(actorRole) },
                        onClick = {
                            role = actorRole
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Téléphone") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                onSaveClick(
                    ExposantFormData(
                        name = name,
                        role = role,
                        phone = phone,
                        email = email,
                        description = description
                    )
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
            enabled = isOnline && name.isNotBlank() && role.isNotBlank()
        ) {
            Text("Enregistrer")
        }
    }
}