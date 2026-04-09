package com.example.clicker.ui.utils.gameUtils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.clicker.ui.theme.ButtonBlue

@Composable
fun GameForm(
    title: String,
    formState: GameFormState,
    onFormChange: (GameFormState) -> Unit,
    onSubmit: () -> Unit,
    submitLabel: String,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = formState.name,
            onValueChange = { onFormChange(formState.copy(name = it)) },
            label = { Text("Nom du jeu *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = formState.author,
            onValueChange = { onFormChange(formState.copy(author = it)) },
            label = { Text("Auteur *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = formState.type,
            onValueChange = { onFormChange(formState.copy(type = it)) },
            label = { Text("Type *") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = formState.editorId,
            onValueChange = { onFormChange(formState.copy(editorId = it)) },
            label = { Text("Editor ID *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = formState.nbMinPlayers,
                onValueChange = { onFormChange(formState.copy(nbMinPlayers = it)) },
                label = { Text("Joueurs min") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = formState.nbMaxPlayers,
                onValueChange = { onFormChange(formState.copy(nbMaxPlayers = it)) },
                label = { Text("Joueurs max") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = formState.ageMin,
                onValueChange = { onFormChange(formState.copy(ageMin = it)) },
                label = { Text("Âge min") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )

            OutlinedTextField(
                value = formState.duree,
                onValueChange = { onFormChange(formState.copy(duree = it)) },
                label = { Text("Durée (min)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Prototype")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(
                checked = formState.prototype,
                onCheckedChange = { onFormChange(formState.copy(prototype = it)) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = formState.description,
            onValueChange = { onFormChange(formState.copy(description = it)) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = formState.notice,
            onValueChange = { onFormChange(formState.copy(notice = it)) },
            label = { Text("Notice") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = formState.imageUrl,
            onValueChange = { onFormChange(formState.copy(imageUrl = it)) },
            label = { Text("URL image") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = formState.videoRulesUrl,
            onValueChange = { onFormChange(formState.copy(videoRulesUrl = it)) },
            label = { Text("URL vidéo règles") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue)
        ) {
            Text(submitLabel)
        }

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}
