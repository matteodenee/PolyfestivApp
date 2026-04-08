package com.example.clicker.ui.screens.festivalList

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.R
import com.example.clicker.data.festival.FestivalDto
import com.example.clicker.ui.viewmodel.AppViewModelProvider

// Custom Mockup Colors
private val AppBg = Color(0xFFFFF8EA)
private val TabOrange = Color(0xFFFF5C33)
private val TabPurple = Color(0xFFEBE6F8)
private val SearchBg = Color(0xFFEDEBF5)
private val TextDark = Color(0xFF2E2A3C)
private val TextPurple = Color(0xFF5B4D8D)
private val DividerColor = Color(0xFFE4E0EC)
private val ShapesColor = Color(0xFFBDB7CB)
private val IconBoxBg = Color(0xFFEBE8F4)

@Composable
fun FestivalScreen(
    refreshKey: Int,
    onFestivalClick: (Int) -> Unit,
    onAddClick: () -> Unit,
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
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Upcoming, 1 = Past
    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(AppBg)
    ) {
        HeaderSection()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Main Titles Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Festivals",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextDark
                )

                Row(
                    modifier = Modifier.clickable { onAddClick() },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ajouter un\nfestival",
                        fontSize = 14.sp,
                        color = Color.DarkGray,
                        lineHeight = 16.sp,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .border(1.dp, TextDark, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Ajouter",
                            tint = TextDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Search Bar
            SearchBarCustom(
                value = searchQuery,
                onValueChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Segmented Tabs
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                // Upcoming
                SegmentTab(
                    text = "Upcoming",
                    isSelected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    modifier = Modifier.weight(1f)
                )
                // Past
                SegmentTab(
                    text = "Past",
                    isSelected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Content List
            when (val uiState = state) {
                is FestivalListUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = TabOrange)
                    }
                }
                is FestivalListUiState.Error -> {
                    Text(uiState.message, color = Color.Red)
                }
                is FestivalListUiState.Success -> {
                    val filteredFestivals = uiState.festivals.filter { festival ->
                        val query = searchQuery.trim().lowercase()
                        query.isBlank() || festival.name.lowercase().contains(query)
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

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButtonCustom(text = "View All Festival", onClick = {})
                            Spacer(modifier = Modifier.height(32.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeaderSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.games_header),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
private fun SearchBarCustom(value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(26.dp)),
        placeholder = { Text("Festival", color = Color.DarkGray) },
        trailingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Recherche", tint = TextDark)
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SearchBg,
            unfocusedContainerColor = SearchBg,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = TextDark
        )
    )
}

@Composable
private fun SegmentTab(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = if (isSelected) TabOrange else TabPurple
    val txtColor = if (isSelected) Color.White else TextDark

    Surface(
        onClick = onClick,
        color = bgColor,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier.height(44.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .padding(end = 6.dp)
                        .size(18.dp)
                )
            }
            Text(text, color = txtColor, fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal)
        }
    }
}

@Composable
private fun FestivalItem(festival: FestivalDto, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Geometric Shapes Icon Box
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(IconBoxBg, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                GeometricShapesCanvas()
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = festival.name,
                    fontSize = 18.sp,
                    color = TextDark,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = festival.startDate.take(10), // Taking just the date portion if necessary
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = festival.description,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Detail Button
            Surface(
                onClick = onClick,
                color = Color.White,
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCD6EF))
            ) {
                Text(
                    text = "Détail",
                    color = TextPurple,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }
        HorizontalDivider(color = DividerColor, thickness = 1.dp)
    }
}

@Composable
private fun OutlinedButtonCustom(text: String, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        shape = RoundedCornerShape(26.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFDCCDB5)),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                color = Color(0xFF4A4652),
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun GeometricShapesCanvas() {
    Canvas(modifier = Modifier.size(46.dp)) {
        val w = size.width
        val h = size.height

        // Triangle
        val pathTriangle = Path().apply {
            moveTo(w / 2, 2.dp.toPx())
            lineTo(w / 2 - 8.dp.toPx(), 18.dp.toPx())
            lineTo(w / 2 + 8.dp.toPx(), 18.dp.toPx())
            close()
        }
        drawPath(pathTriangle, ShapesColor)

        // Square
        drawRect(
            color = ShapesColor,
            topLeft = Offset(w / 2 + 4.dp.toPx(), h / 2 + 2.dp.toPx()),
            size = Size(14.dp.toPx(), 14.dp.toPx())
        )

        // Spiky Circle (Sun/Gear)
        val cx = w / 2 - 8.dp.toPx()
        val cy = h / 2 + 8.dp.toPx()
        val radius = 5.dp.toPx()
        drawCircle(color = ShapesColor, radius = radius, center = Offset(cx, cy))
        // Drawing spikes
        for (i in 0 until 8) {
            val angle = i * (Math.PI / 4)
            val outerRadius = radius + 3.dp.toPx()
            drawLine(
                color = ShapesColor,
                start = Offset(cx, cy),
                end = Offset(
                    (cx + outerRadius * kotlin.math.cos(angle)).toFloat(),
                    (cy + outerRadius * kotlin.math.sin(angle)).toFloat()
                ),
                strokeWidth = 3f
            )
        }
    }
}
