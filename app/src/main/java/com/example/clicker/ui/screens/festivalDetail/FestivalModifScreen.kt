package com.example.clicker.ui.screens.festivalDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun FestivalModifScreen(
    festivalId: Int,
    onBackClick: () -> Unit,
    onStepClick: (stepNumber: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FestivalDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(festivalId) {
        viewModel.loadFestival(festivalId)
    }

    val festivalName = when (val state = viewModel.state.value) {
        is FestivalDetailUiState.Success -> state.festival.name
        else -> "Festival"
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFFFF9E1)) // Light yellowish background
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Modification",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))

        val steps = listOf(
            "Etape 1" to "Détail festival",
            "Etape 2" to "Stock tables",
            "Etape 3" to "Stock matériel",
            "Etape 4" to "Zones tarifaires",
            "Etape 5" to "Zones du plan"
        )

        steps.forEachIndexed { index, step ->
            ModifStepItem(
                title = step.first,
                subtitle = step.second,
                onClick = { onStepClick(index + 1) }
            )

            if (index < steps.size - 1) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
fun ModifStepItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFE8E8F4)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Color.LightGray
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
    }
}
