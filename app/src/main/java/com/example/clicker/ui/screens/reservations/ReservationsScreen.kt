package com.example.clicker.ui.screens.reservations

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.reservation.ReservationDto
import com.example.clicker.data.reservation.statusLabel
import com.example.clicker.ui.theme.SearchField
import com.example.clicker.ui.viewmodel.AppViewModelProvider
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.clicker.R

@Composable
fun ReservationsScreen(
    festivalId: Int,
    refreshKey: Int,
    onAddClick: () -> Unit,
    onReservationClick: (ReservationDto) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReservationsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(festivalId, refreshKey) {
        viewModel.loadReservations(festivalId)
    }

    val state = viewModel.state.value
    var searchQuery by remember { mutableStateOf("") }
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()

    val maxHeaderHeight = 165.dp
    val minHeaderHeight = 80.dp
    val density = LocalDensity.current
    val targetHeaderHeight by remember {
        derivedStateOf {
            if (listState.firstVisibleItemIndex > 0) {
                minHeaderHeight
            } else {
                val scrollOffsetPx = listState.firstVisibleItemScrollOffset
                val scrollOffsetDp = with(density) { scrollOffsetPx.toDp() }
                (maxHeaderHeight - scrollOffsetDp).coerceAtLeast(minHeaderHeight)
            }
        }
    }

    val headerHeight by animateDpAsState(
        targetValue = targetHeaderHeight,
        label = "reservationHeaderHeight"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        ReservationHeader(
            height = headerHeight,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = viewModel.festivalName,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onBackground
                    )
                )

                Surface(
                    onClick = onAddClick,
                    color = Color.Transparent
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Nouvelle réservation",
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.onBackground
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "Ajouter une réservation",
                            tint = colors.onBackground
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            ReservationSearchBar(
                value = searchQuery,
                onValueChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            when (val uiState = state) {
                is ReservationsUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.primary)
                    }
                }

                is ReservationsUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is ReservationsUiState.Success -> {
                    val query = searchQuery.trim().lowercase()
                    val filteredReservations = uiState.reservations.filter { reservation ->
                        val reservantName =
                            viewModel.getReservantName(reservation.reservantId).lowercase()

                        query.isBlank() ||
                                reservation.id.toString().contains(query) ||
                                reservation.reservantId.toString().contains(query) ||
                                reservantName.contains(query) ||
                                reservation.statusLabel().lowercase().contains(query)
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filteredReservations) { reservation ->
                            ReservationItem(
                                reservation = reservation,
                                reservantName = viewModel.getReservantName(reservation.reservantId),
                                onClick = { onReservationClick(reservation) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ReservationHeader(
    height: Dp,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Image(
            painter = painterResource(R.drawable.games_header),
            contentDescription = "Header jeux",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}


@Composable
private fun ReservationSearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    val colors = MaterialTheme.colorScheme

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(27.dp)),
        placeholder = {
            Text(
                text = "Rechercher une réservation",
                color = colors.onSurface.copy(alpha = 0.7f)
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Recherche",
                tint = colors.onSurface
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(27.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SearchField,
            unfocusedContainerColor = SearchField,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = colors.onSurface
        )
    )
}

@Composable
private fun ReservationItem(
    reservation: ReservationDto,
    reservantName: String,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val statusColor = when (reservation.status) {
        0 -> Color(0xFF9E9E9E)
        1 -> Color(0xFF2196F3)
        2 -> Color(0xFFFF9800)
        3 -> Color(0xFF607D8B)
        4 -> Color(0xFFF44336)
        5 -> Color(0xFF4CAF50)
        6 -> Color(0xFF9C27B0)
        7 -> Color(0xFF2E7D32)
        else -> Color(0xFFBDBDBD)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp)
    ) {
        Text(
            text = "Réservation ${reservation.id}",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = colors.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = reservantName,
            style = MaterialTheme.typography.bodySmall,
            color = colors.onBackground.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        AssistChip(
            onClick = {},
            label = {
                Text(
                    text = reservation.statusLabel(),
                    color = Color.White
                )
            },
            shape = CircleShape,
            colors = AssistChipDefaults.assistChipColors(
                containerColor = statusColor
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(color = colors.outline.copy(alpha = 0.15f))
    }
}