package com.example.clicker.ui.screens.exposants

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.clicker.data.model.Exposant
import com.example.clicker.data.model.ExposantFormData
import com.example.clicker.data.model.toFormData
import com.example.clicker.ui.theme.AccentBlue
import com.example.clicker.ui.theme.BackgroundCream
import com.example.clicker.ui.theme.BorderLilac
import androidx.compose.ui.unit.dp

@Composable
fun ExposantFormScreen(
    mode: ExposantFormMode,
    exposant: Exposant?,
    onBackClick: () -> Unit,
    onSaveClick: (ExposantFormData) -> Unit,
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
        "Ajout exposant"
    } else {
        "Modification exposant"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundCream)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Outlined.ArrowBack,
                    contentDescription = "Retour"
                )
            }

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        ExposantTextField(
            value = name,
            onValueChange = { name = it },
            label = "Nom"
        )

        ExposantTextField(
            value = role,
            onValueChange = { role = it },
            label = "Rôle"
        )

        ExposantTextField(
            value = phone,
            onValueChange = { phone = it },
            label = "Téléphone"
        )

        ExposantTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email"
        )

        ExposantTextField(
            value = description,
            onValueChange = { description = it },
            label = "Description",
            minLines = 4
        )

        Spacer(modifier = Modifier.height(12.dp))

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
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentBlue),
            enabled = name.isNotBlank()
        ) {
            Text("Valider")
        }
    }
}

@Composable
private fun ExposantTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        shape = RoundedCornerShape(16.dp),
        minLines = minLines,
        colors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.primary,
        cursorColor = MaterialTheme.colorScheme.primary
    )
    )
}