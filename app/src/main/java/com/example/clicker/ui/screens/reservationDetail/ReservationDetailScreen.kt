package com.example.clicker.ui.screens.reservationDetail

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.ContactPhone
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.reservation.ReservationDto
import com.example.clicker.data.reservation.statusLabel
import com.example.clicker.ui.theme.ButtonBlue
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun ReservationDetailScreen(
    reservation: ReservationDto,
    onDeleteSuccess: () -> Unit,
    onSuppliesClick: (reservationId: Int, festivalId: Int) -> Unit,
    onInvoiceClick: (Int) -> Unit,
    onContactClick: (Int) -> Unit,
    onNoteClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReservationDetailViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.state
    val reservantName by viewModel.reservantName
    val reservantType by viewModel.reservantType

    LaunchedEffect(reservation.reservantId) {
        viewModel.loadReservantInfo(reservation.reservantId)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            ReservationMainInfo(
                reservation = reservation,
                reservantName = reservantName,
                reservantType = reservantType
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    viewModel.deleteReservation(
                        reservationId = reservation.id,
                        onDeleted = onDeleteSuccess
                    )
                },
                enabled = uiState !is ReservationDetailUiState.Loading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFF6B4A),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(18.dp)
            ) {
                if (uiState is ReservationDetailUiState.Loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = Color.White
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Supprimer"
                    )
                    Spacer(modifier = Modifier.size(8.dp))
                    Text("Supprimer")
                }
            }

            if (uiState is ReservationDetailUiState.Error) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = (uiState as ReservationDetailUiState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ReservationTilesGrid(
                reservationId = reservation.id,
                festivalId = reservation.festivalId,
                onSuppliesClick = onSuppliesClick,
                onInvoiceClick = onInvoiceClick,
                onContactClick = onContactClick,
                onNoteClick = onNoteClick
            )
        }
    }
}

@Composable
private fun ReservationMainInfo(
    reservation: ReservationDto,
    reservantName: String,
    reservantType: String
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Réservation ${reservation.id}",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Réservant : $reservantName",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Type du réservant : $reservantType",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "État du workflow : ${reservation.statusLabel()}",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Tables demandées : ${reservation.freeTables ?: 0}",
            style = MaterialTheme.typography.bodyMedium,
            color = colors.onBackground
        )
    }
}

@Composable
private fun ReservationTilesGrid(
    reservationId: Int,
    festivalId: Int,
    onSuppliesClick: (reservationId: Int, festivalId: Int) -> Unit,
    onInvoiceClick: (Int) -> Unit,
    onContactClick: (Int) -> Unit,
    onNoteClick: (Int) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ReservationTile(
                modifier = Modifier.weight(1f),
                title = "Fournitures",
                icon = Icons.Default.Inventory2,
                onClick = { onSuppliesClick(reservationId, festivalId) }
            )

            ReservationTile(
                modifier = Modifier.weight(1f),
                title = "Facture",
                icon = Icons.Default.Article,
                onClick = { onInvoiceClick(reservationId) }
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            ReservationTile(
                modifier = Modifier.weight(1f),
                title = "Prise de contact",
                icon = Icons.Default.ContactPhone,
                onClick = { onContactClick(reservationId) }
            )

            ReservationTile(
                modifier = Modifier.weight(1f),
                title = "Note",
                icon = Icons.Default.Description,
                onClick = { onNoteClick(reservationId) }
            )
        }
    }
}

@Composable
private fun ReservationTile(
    title: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = MaterialTheme.colorScheme

    Card(
        modifier = modifier
            .aspectRatio(1f)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = colors.surfaceVariant.copy(alpha = 0.55f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = colors.surface
            ) {
                Box(
                    modifier = Modifier.padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = ButtonBlue,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.onBackground,
                fontWeight = FontWeight.Medium
            )
        }
    }
}