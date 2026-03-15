package com.example.clicker.ui.screens

import android.app.Activity
import androidx.activity.ComponentActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Celebration
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.clicker.ui.screens.exposants.ExposantsScreen
import com.example.clicker.ui.theme.PrimaryYellow

class MainScreen : ComponentActivity() {

    enum class Destination(
        val route: String,
        val label: String,
        val icon: ImageVector,
        val contentDescription: String,
    ) {
        FESTIVALS(
            route = "festivals",
            label = "Festivals",
            icon = Icons.Default.Celebration,
            contentDescription = "Festival"
        ),
        JEUX(
            route = "jeux",
            label = "Jeux",
            icon = Icons.Default.Casino,
            contentDescription = "Jeux"
        ),
        EXPOSANTS(
            route = "exposants",
            label = "Exposants",
            icon = Icons.Default.Storefront,
            contentDescription = "Exposants"
        )
    }
}

@Composable
fun Clicker(
    mainScreenViewModel: MainScreenViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val uiState by mainScreenViewModel.uiState.collectAsState()
    val score = uiState.currentScore

    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = score.toString())

        Row {
            Surface(
                modifier = Modifier.clickable { mainScreenViewModel.decrement() }
            ) {
                Text(
                    text = "-",
                    modifier = Modifier.padding(16.dp)
                )
            }

            Surface(
                modifier = Modifier.clickable { mainScreenViewModel.increment() }
            ) {
                Text(
                    text = "+",
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmallNavigationExample() {
    val backStack = rememberSaveable {
        mutableStateListOf(MainScreen.Destination.FESTIVALS)
    }

    val currentDestination = backStack.lastOrNull() ?: MainScreen.Destination.FESTIVALS

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text(
                        text = currentDestination.label
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (backStack.size > 1) {
                                backStack.removeLastOrNull()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    MainScreen.Destination.entries.forEach { destination ->
                        NavigationBarItem(
                            selected = currentDestination == destination,
                            onClick = {
                                if (currentDestination != destination) {
                                    backStack.add(destination)
                                }
                            },
                            icon = {
                                Icon(
                                    imageVector = destination.icon,
                                    contentDescription = destination.contentDescription
                                )
                            },
                            label = {
                                Text(destination.label)
                            },
                            colors = NavigationBarItemDefaults.colors(
                                indicatorColor = PrimaryYellow
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = {
                if (backStack.size > 1) {
                    backStack.removeLastOrNull()
                }
            },
            entryProvider = { key ->
                when (key) {
                    MainScreen.Destination.FESTIVALS -> NavEntry(key) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Clicker()
                            }
                        }
                    }

                    MainScreen.Destination.JEUX -> NavEntry(key) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Jeux")
                        }
                    }

                    MainScreen.Destination.EXPOSANTS -> NavEntry(key) {
                        ExposantsScreen(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    else -> NavEntry(key) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Error")
                        }
                    }
                }
            }
        )
    }
}