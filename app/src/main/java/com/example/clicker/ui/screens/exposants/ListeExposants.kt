package com.example.clicker.ui.screens.exposants

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.clicker.data.model.Exposant
import com.example.clicker.data.model.typeLabel
import com.example.clicker.ui.theme.AccentBlue
import com.example.clicker.ui.theme.BorderLilac
import com.example.clicker.ui.theme.CardLilac
import com.example.clicker.ui.theme.TextSecondary
import androidx.compose.ui.unit.dp

@Composable
fun ListeExposants(
    exposants: List<Exposant>,
    onDetailsClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(bottom = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = exposants,
            key = { exposant -> exposant.id }
        ) { exposant ->
            ExposantItem(
                exposant = exposant,
                onDetailsClick = { onDetailsClick(exposant.id) }
            )
        }
    }
}

@Composable
private fun ExposantItem(
    exposant: Exposant,
    onDetailsClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardLilac),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .background(MaterialTheme.colorScheme.background, CircleShape)
                    .border(1.dp, BorderLilac, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = exposant.name.take(1).uppercase(),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = exposant.name,
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = exposant.typeLabel(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                if (!exposant.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = exposant.description!!,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        maxLines = 2
                    )
                }
            }

            Spacer(modifier = Modifier.size(8.dp))

            Button(
                onClick = onDetailsClick,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
            ) {
                Text("Détail")
            }
        }
    }
}