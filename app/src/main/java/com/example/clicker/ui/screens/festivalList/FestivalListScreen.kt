package com.example.clicker.ui.screens.festivalList

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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Festival
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.R
import com.example.clicker.data.festival.FestivalDto
import com.example.clicker.ui.theme.SearchField
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun FestivalScreen(
    refreshKey: Int,
    onFestivalClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FestivalListViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    LaunchedEffect(refreshKey) {
        viewModel.loadFestivals()
    }

    val state = viewModel.state.value
    var searchQuery by remember { mutableStateOf("") }
    val colors = MaterialTheme.colorScheme

    val listState = rememberLazyListState()

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
        HeaderSection(height = headerHeight)

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
                    text = "Festivals",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onBackground
                    )
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            SearchBar(
                value = searchQuery,
                onValueChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            when (val uiState = state) {
                is FestivalListUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.primary)
                    }
                }

                is FestivalListUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is FestivalListUiState.Success -> {
                    val filteredFestivals = uiState.festivals.filter { festival ->
                        val query = searchQuery.trim().lowercase()
                        query.isBlank() ||
                                festival.name.lowercase().contains(query) ||
                                festival.description.lowercase().contains(query)
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filteredFestivals) { festival ->
                            FestivalItem(
                                festival = festival,
                                onClick = { onFestivalClick(festival.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(height: Dp) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
    ) {
        // Re-using games_header for now as no specific festival header was found
        Image(
            painter = painterResource(R.drawable.games_header),
            contentDescription = "Header festivals",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun SearchBar(
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
                text = "Rechercher un festival",
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
private fun FestivalItem(
    festival: FestivalDto,
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
                    imageVector = Icons.Default.Festival,
                    contentDescription = null,
                    tint = colors.outline,
                    modifier = Modifier.size(34.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = festival.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = colors.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${festival.startDate} - ${festival.endDate}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = colors.onBackground.copy(alpha = 0.6f)
                        )
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = festival.description,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = colors.onBackground.copy(alpha = 0.8f)
                    ),
                    maxLines = 2
                )
            }

            Surface(
                onClick = onClick,
                shape = RoundedCornerShape(24.dp),
                color = colors.secondary,
                tonalElevation = 0.dp,
                shadowElevation = 4.dp
            ) {
                Text(
                    text = "Détail",
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    color = colors.onSecondary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold
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
