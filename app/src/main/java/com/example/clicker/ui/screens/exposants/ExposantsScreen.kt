package com.example.clicker.ui.screens.exposants

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.R
import com.example.clicker.data.exposants.Exposant
import com.example.clicker.data.exposants.typeLabel
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun ExposantsScreen(
    modifier: Modifier = Modifier,
    exposantViewModel: ExposantViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onAddClick: () -> Unit,
    onDetailsClick: (Int) -> Unit
) {
    val uiState = exposantViewModel.uiState.collectAsState().value
    val colors = MaterialTheme.colorScheme
    val listState = rememberLazyListState()

    LaunchedEffect(Unit) {
        exposantViewModel.loadExposants()
    }

    val maxHeaderHeight = 215.dp
    val minHeaderHeight = 90.dp
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
        label = "headerHeight"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        ExposantsHeaderSection(height = headerHeight)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 22.dp)
        ) {
            when (val state = uiState) {
                ExposantUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.primary)
                    }
                }

                is ExposantUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is ExposantUiState.Success -> {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Exposants",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = colors.onBackground
                            )
                        )

                        AddExposantButton(
                            enabled = state.isOnline,
                            onClick = onAddClick
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.weight(1f)
                    ) {
                        items(state.exposants) { exposant ->
                            ExposantItem(
                                exposant = exposant,
                                onClick = { onDetailsClick(exposant.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AddExposantButton(
    enabled: Boolean,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme
    val contentAlpha = if (enabled) 1f else 0.45f

    Surface(
        onClick = {
            if (enabled) onClick()
        },
        color = Color.Transparent,
        modifier = Modifier.alpha(contentAlpha)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
        ) {
            Text(
                text = "Ajouter un exposant",
                color = colors.onBackground,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.width(6.dp))

            Icon(
                imageVector = Icons.Default.AddCircleOutline,
                contentDescription = "Ajouter un exposant",
                tint = colors.onBackground
            )
        }
    }
}

@Composable
private fun ExposantsHeaderSection(height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        Image(
            painter = painterResource(id = R.drawable.exposants_header),
            contentDescription = "Header exposants",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun ExposantItem(
    exposant: Exposant,
    onClick: () -> Unit
) {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(82.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(colors.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = null,
                    tint = colors.outline,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = exposant.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Type : ${exposant.typeLabel()}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colors.onBackground.copy(alpha = 0.8f)
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = colors.outlineVariant
        )
    }
}