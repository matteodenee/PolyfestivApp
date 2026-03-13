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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlaylistAddCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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

class MainScreen : ComponentActivity() {
    enum class Destination(
        val route: String,
        val label: String,
        val icon: ImageVector,
        val contentDescription: String,
    ) {
//        SONGS("songs", "Songs", Icons.Default.MusicNote, "Songs"),
//        ALBUM("album", "Album", Icons.Default.Album, "Album"),
//        PLAYLISTS("playlist", "Playlist", Icons.Default.PlaylistAddCircle, "Playlist"),
        HOME(route="home", label="Home", Icons.Default.Home, contentDescription = "Home"),
        PROFILE(route="profile", label="Profile", Icons.Default.Person, contentDescription = "Profile"),
        SETTINGS(route="settings", label="Settings", Icons.Default.Settings, contentDescription = "Settings")
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
    val backStack = rememberSaveable() { mutableStateListOf(MainScreen.Destination.HOME) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
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
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
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
                            label = { Text(destination.label) }
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
                    MainScreen.Destination.HOME -> NavEntry(key)
                    {
                        Surface(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier.padding(vertical = 100.dp),
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Clicker()
                            }
                        }
                    }

                    MainScreen.Destination.PROFILE -> NavEntry(key)
                    {
                        Box(modifier = Modifier.padding(paddingValues = innerPadding))
                        {
                            Text("Profile")
                        }
                    }

                    MainScreen.Destination.SETTINGS -> NavEntry(key)
                    {
                        Box(modifier = Modifier.padding(paddingValues = innerPadding))
                        {
                            Text("Settings")
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