package com.example.clicker.ui.screens

import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Games
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlaylistAddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.clicker.ui.theme.PrimaryYellow

class MainScreen : ComponentActivity() {
    enum class Destination(
        val route: String,
        val label: String,
        val icon: ImageVector,
        val contentDescription: String,
    ) {
        FESTIVALS(route="festivals", label="Festivals", Icons.Default.Celebration, contentDescription = "Festival"),
        JEUX(route="jeux", label="Jeux", Icons.Default.Casino, contentDescription = "Jeux"),
        EXPOSANTS(route="exposants", label="Exposants", Icons.Default.Storefront, contentDescription = "Exposants")
    }
}

@Composable
fun Clicker(mainScreenViewModel: MainScreenViewModel = viewModel(), modifier: Modifier = Modifier) {
    Column(modifier = modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        val uiState by mainScreenViewModel.uiState.collectAsState()
        var score = uiState.currentScore
        Text(text = score.toString())

        Row {
            Surface(modifier = Modifier.clickable { mainScreenViewModel.decrement() }) {
                Text(text = "-", modifier = Modifier.padding(16.dp))

            }
            Surface(modifier = Modifier.clickable { mainScreenViewModel.increment() }) {
                Text(text = "+", modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallNavigationExample() {
    val backStack = rememberSaveable() { mutableStateListOf(MainScreen.Destination.FESTIVALS) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text("Small Top App Bar")
                },
                navigationIcon = {
                    IconButton(onClick = { backStack.removeLastOrNull() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    MainScreen.Destination.entries.forEachIndexed { index, destination ->
                        NavigationBarItem(
                            selected = true,
                            onClick = { backStack.add(destination)
                            },
                            icon = {
                                Icon(
                                    destination.icon,
                                    contentDescription = destination.contentDescription
                                )
                            },
                            label = { Text(destination.label) },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = PrimaryYellow,
                            )

                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = { key ->
                when (key) {
                    MainScreen.Destination.FESTIVALS -> NavEntry(key)
                    {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 100.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Clicker()
                            }
                        }
                    }

                    MainScreen.Destination.JEUX -> NavEntry(key)
                    {
                        Box(modifier = Modifier.padding(paddingValues = innerPadding))
                        {
                            Text("Jeux")
                        }
                    }

                    MainScreen.Destination.EXPOSANTS -> NavEntry(key)
                    {
                        Box(modifier = Modifier.padding(paddingValues = innerPadding))
                        {
                            Text("Exposants")
                        }
                    }

                    else -> NavEntry(key)
                    {
                        Box(modifier = Modifier.padding(paddingValues = innerPadding))
                        {
                            Text("Error")
                        }
                    }

                }
            }
        )
    }
}