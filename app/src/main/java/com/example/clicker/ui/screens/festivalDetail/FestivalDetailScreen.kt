package com.example.clicker.ui.screens.festivalDetail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.clicker.ui.theme.ButtonBlue
import com.example.clicker.ui.viewmodel.AppViewModelProvider

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun formatFestivalDate(isoString: String): String {
    return try {
        val parsedDate = ZonedDateTime.parse(isoString)
        val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.FRENCH)
        parsedDate.format(formatter)
    } catch (e: Exception) {
        isoString
    }
}

@Composable
fun FestivalMenuItem(
    label: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE8E8F4)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Category,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.LightGray
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = label,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
fun FestivalMenuGrid(
    items: List<Pair<String, () -> Unit>>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        items.chunked(3).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { (label, onClick) ->
                    Box(modifier = Modifier.weight(1f)) {
                        FestivalMenuItem(label = label, onClick = onClick)
                    }
                }
                repeat(3 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun FestivalDetailScreen(
    festivalId: Int,
    refreshKey: Int,
    onEditClick: (Int) -> Unit,
    onReservationsClick: (Int) -> Unit,
    onPlacementClick: (Int) -> Unit,
    onDeleteSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FestivalDetailViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    LaunchedEffect(festivalId, refreshKey) {
        viewModel.loadFestival(festivalId)
    }

    when (val uiState = viewModel.state.value) {
        is FestivalDetailUiState.Loading -> {
            Column(
                modifier = modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is FestivalDetailUiState.Error -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }

        is FestivalDetailUiState.Success -> {
            val festival = uiState.festival

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                Text(
                    text = festival.name,
                    style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Du ${formatFestivalDate(festival.startDate)} au ${formatFestivalDate(festival.endDate)}")

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { onEditClick(festival.id) },
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBlue),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Modifier", color = Color.White)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text("Détail", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                
                Text("Dates : ${formatFestivalDate(festival.startDate)} - ${formatFestivalDate(festival.endDate)}")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Capacité totale : ")
                Text("Capacité espace (m² - tables) : ")
                Text("Espace restant (estimation) : ")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Tables disponibles (zones) :")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Zones tarifaires :")
                Text("Tables prévues : ")

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    "Gérer le festival",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                FestivalMenuGrid(
                    items = listOf(
                        "Réservation" to { onReservationsClick(festival.id) },
                        "Placement jeux" to { onPlacementClick(festival.id) },
                        "Jeux du festival" to { /* navigate */ },
                        "Exposants du festival" to { /* navigate */ },
                        "Plan public" to { /* navigate */ },
                    )
                )
            }
        }
    }
}
