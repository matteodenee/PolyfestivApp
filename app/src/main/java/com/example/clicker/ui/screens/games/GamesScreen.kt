package com.example.clicker.ui.screens.games

import android.util.Log
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
import androidx.compose.material.icons.filled.Extension
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
import coil3.compose.AsyncImage
import com.example.clicker.R
import com.example.clicker.data.game.GameDto
import com.example.clicker.ui.theme.SearchField
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun GamesScreen(
    refreshKey: Int,
    canAdd: Boolean,
    onAddClick: () -> Unit,
    onGameClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: GamesViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    LaunchedEffect(refreshKey) {
        viewModel.loadGames()
    }

    val state = viewModel.state.value
    var searchQuery by remember { mutableStateOf("") }
    val colors = MaterialTheme.colorScheme

    val listState = rememberLazyListState()// permet de savoir à quelle position on est dans la liste

    val maxHeaderHeight = 215.dp
    val minHeaderHeight = 90.dp
    val density = LocalDensity.current
    val targetHeaderHeight by remember {// hauteur cible du header
        derivedStateOf {
            // Si on a scrollé au-delà du premier élément
            if (listState.firstVisibleItemIndex > 0) {
                // On bloque directement à la hauteur minimale
                minHeaderHeight
            } else {
                // Sinon, on est encore sur le premier item donc on réduit progressivement le header
                val scrollOffsetPx = listState.firstVisibleItemScrollOffset
                val scrollOffsetDp = with(density) { scrollOffsetPx.toDp() }
                // Calcul de la nouvelle hauteur :
                (maxHeaderHeight - scrollOffsetDp)
                    .coerceAtLeast(minHeaderHeight)
            }
        }
    }

    // Animation de la hauteur du header
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
                    text = "Jeux",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onBackground
                    )
                )

                Surface(
                    onClick = onAddClick,
                    enabled = canAdd,
                    color = Color.Transparent
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Ajouter un jeu",
                            color = if (canAdd) {
                                colors.onBackground
                            } else { // bouton grisé
                                colors.onBackground.copy(alpha = 0.4f)
                            },
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "Ajouter un jeu",
                            tint = if (canAdd) {
                                colors.onBackground
                            } else {
                                colors.onBackground.copy(alpha = 0.4f)
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            SearchBar(
                value = searchQuery,
                onValueChange = { searchQuery = it }
            )

            Spacer(modifier = Modifier.height(14.dp))

            when (val uiState = state) {
                is GamesUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = colors.primary)
                    }
                }

                is GamesUiState.Error -> {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                is GamesUiState.Success -> {
                    val filteredGames = uiState.games.filter { game ->
                        val query = searchQuery.trim().lowercase()
                        query.isBlank() ||
                                game.name.lowercase().contains(query) ||
                                game.author.lowercase().contains(query)
                    }

                    LazyColumn(
                        state = listState,
                        modifier = Modifier.weight(1f)
                    ) {
                        items(filteredGames) { game ->
                            GameItem(
                                game = game,
                                onClick = { onGameClick(game.id) }
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
        Image(
            painter = painterResource(R.drawable.games_header),
            contentDescription = "Header jeux",
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
                text = "Rechercher un jeu",
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
private fun GameItem(
    game: GameDto,
    onClick: () -> Unit
) {
    val imageUrl = game.imageUrl.trim()
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
            if (imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Image de ${game.name}",
                    modifier = Modifier
                        .size(82.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(colors.surfaceVariant),
                    contentScale = ContentScale.Crop,
                    onError = { state ->
                        Log.e(
                            "GAME_IMAGE",
                            "Erreur chargement URL = $imageUrl",
                            state.result.throwable
                        )
                    },
                    onSuccess = {
                        Log.d(
                            "GAME_IMAGE",
                            "Succès chargement URL = $imageUrl"
                        )
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(colors.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Extension,
                        contentDescription = null,
                        tint = colors.outline,
                        modifier = Modifier.size(34.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = game.name,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = colors.onBackground
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Auteur: ${game.author}",
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