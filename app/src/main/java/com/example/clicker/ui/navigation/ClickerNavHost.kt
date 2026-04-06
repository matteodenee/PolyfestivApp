package com.example.clicker.ui.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.clicker.ui.screens.gameCreate.GameCreateScreen
import com.example.clicker.ui.screens.gameDetail.GameDetailScreen
import com.example.clicker.ui.screens.gameEdit.GameEditScreen
import com.example.clicker.ui.screens.games.GamesScreen
import com.example.clicker.ui.screens.login.LoginScreen
import com.example.clicker.ui.screens.login.LoginViewModel
import com.example.clicker.ui.screens.register.RegisterScreen
import com.example.clicker.ui.screens.festivalList.FestivalScreen
import com.example.clicker.ui.screens.festivalDetail.FestivalDetailScreen
import com.example.clicker.ui.screens.festivalDetail.FestivalModifScreen
import com.example.clicker.ui.screens.festivalList.FestivalCreateScreen
import com.example.clicker.ui.theme.PrimaryYellow
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickerNavHost(
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val backStack = remember { mutableStateListOf<Any>(AppRoutes.LOGIN) }

    var gamesRefreshKey by remember { mutableIntStateOf(0) }
    var festivalsRefreshKey by remember { mutableIntStateOf(0) }
    var detailRefreshKey by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        loginViewModel.restoreSessionIfNeeded {
            backStack.clear()
            backStack.add(Destination.FESTIVALS)
        }
    }

    val currentDestination = backStack.lastOrNull()
    val shouldDisplayBars = currentDestination !in listOf(AppRoutes.LOGIN, AppRoutes.REGISTER)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (shouldDisplayBars) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    title = {
                        Text(
                            when (currentDestination) {
                                Destination.FESTIVALS -> "Festivals"
                                Destination.JEUX -> "Jeux"
                                Destination.EXPOSANTS -> "Exposants"
                                AppRoutes.GameCreateRoute -> "Ajout"
                                is AppRoutes.GameDetailRoute -> "Détail"
                                is AppRoutes.GameEditRoute -> "Modification"
                                AppRoutes.FestivalCreateRoute -> "Nouveau Festival"
                                is AppRoutes.FestivalDetailRoute -> "Détail Festival"
                                is AppRoutes.FestivalModifRoute -> "Modification"
                                is AppRoutes.ModifDetailRoute -> "Détail festival"
                                is AppRoutes.StockTablesRoute -> "Stock tables"
                                is AppRoutes.StockMaterielRoute -> "Stock matériel"
                                is AppRoutes.ZonesTarifRoute -> "Zones tarifaires"
                                is AppRoutes.ZonesPlanRoute -> "Zones du plan"
                                is AppRoutes.GenericEditRoute -> currentDestination.screenTitle.replace("Plan ", "")
                                else -> "Clicker"
                            }
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
            }
        },
        bottomBar = {
            if (shouldDisplayBars) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Destination.entries.forEach { destination ->
                            NavigationBarItem(
                                selected = isDestinationSelected(currentDestination, destination),
                                onClick = {
                                    if (!isDestinationSelected(currentDestination, destination)) {
                                        backStack.add(destination)
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = destination.icon,
                                        contentDescription = destination.contentDescription
                                    )
                                },
                                label = { Text(destination.label) },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = PrimaryYellow
                                )
                            )
                        }
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
                    AppRoutes.LOGIN -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            LoginScreen(
                                onLoginSuccess = {
                                    backStack.clear()
                                    backStack.add(Destination.FESTIVALS)
                                },
                                onNavigateToRegister = {
                                    backStack.add(AppRoutes.REGISTER)
                                }
                            )
                        }
                    }

                    AppRoutes.REGISTER -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            RegisterScreen(
                                onRegisterSuccess = {
                                    if (backStack.size > 1) {
                                        backStack.removeLastOrNull()
                                    }
                                },
                                onNavigateToLogin = {
                                    if (backStack.size > 1) {
                                        backStack.removeLastOrNull()
                                    }
                                }
                            )
                        }
                    }

                    Destination.FESTIVALS -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            FestivalScreen(
                                refreshKey = festivalsRefreshKey,
                                onFestivalClick = { festivalId ->
                                    backStack.add(AppRoutes.FestivalDetailRoute(festivalId))
                                },
                                onAddClick = {
                                    backStack.add(AppRoutes.FestivalCreateRoute)
                                }
                            )
                        }
                    }

                    Destination.JEUX -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            GamesScreen(
                                refreshKey = gamesRefreshKey,
                                onGameClick = { gameId ->
                                    backStack.add(AppRoutes.GameDetailRoute(gameId))
                                },
                                onAddClick = {
                                    backStack.add(AppRoutes.GameCreateRoute)
                                }
                            )
                        }
                    }

                    Destination.EXPOSANTS -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            Text("Exposants")
                        }
                    }

                    AppRoutes.GameCreateRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            GameCreateScreen(
                                onCreateSuccess = {
                                    gamesRefreshKey++
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                    }

                    is AppRoutes.GameDetailRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            GameDetailScreen(
                                gameId = key.gameId,
                                refreshKey = detailRefreshKey,
                                onEditClick = { gameId ->
                                    backStack.add(AppRoutes.GameEditRoute(gameId))
                                },
                                onDeleteSuccess = {
                                    gamesRefreshKey++
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                    }

                    is AppRoutes.GameEditRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            GameEditScreen(
                                gameId = key.gameId,
                                onEditSuccess = {
                                    gamesRefreshKey++
                                    detailRefreshKey++
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                    }

                    AppRoutes.FestivalCreateRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            FestivalCreateScreen(
                                onCreateSuccess = { festivalId ->
                                    festivalsRefreshKey++
                                    backStack.removeLastOrNull() // Pop create screen
                                    backStack.add(AppRoutes.FestivalDetailRoute(festivalId)) // Go to detail
                                }
                            )
                        }
                    }

                    is AppRoutes.FestivalDetailRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            FestivalDetailScreen(
                                festivalId = key.festivalId,
                                refreshKey = detailRefreshKey,
                                onEditClick = { festivalId ->
                                    backStack.add(AppRoutes.FestivalModifRoute(festivalId))
                                },
                                onDeleteSuccess = {
                                    festivalsRefreshKey++
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                    }

                    is AppRoutes.FestivalModifRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            FestivalModifScreen(
                                festivalId = key.festivalId,
                                onBackClick = {
                                    backStack.removeLastOrNull()
                                },
                                onStepClick = { step ->
                                    when (step) {
                                        1 -> backStack.add(AppRoutes.ModifDetailRoute(key.festivalId))
                                        2 -> backStack.add(AppRoutes.StockTablesRoute(key.festivalId))
                                        3 -> backStack.add(AppRoutes.StockMaterielRoute(key.festivalId))
                                        4 -> backStack.add(AppRoutes.ZonesTarifRoute(key.festivalId))
                                        5 -> backStack.add(AppRoutes.ZonesPlanRoute(key.festivalId))
                                    }
                                }
                            )
                        }
                    }

                    is AppRoutes.ModifDetailRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            com.example.clicker.ui.screens.festivalEdit.ModifDetailScreen(
                                festivalId = key.festivalId,
                                onBackClick = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                    }

                    is AppRoutes.StockTablesRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            com.example.clicker.ui.screens.festivalEdit.StockTablesScreen(
                                festivalId = key.festivalId,
                                onNavigateToEdit = { name ->
                                    backStack.add(AppRoutes.GenericEditRoute(name))
                                }
                            )
                        }
                    }

                    is AppRoutes.StockMaterielRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            com.example.clicker.ui.screens.festivalEdit.StockMaterielScreen(
                                festivalId = key.festivalId,
                                onNavigateToEdit = { name ->
                                    backStack.add(AppRoutes.GenericEditRoute(name))
                                }
                            )
                        }
                    }

                    is AppRoutes.ZonesTarifRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            com.example.clicker.ui.screens.festivalEdit.ZonesTarifScreen(
                                festivalId = key.festivalId,
                                onNavigateToEdit = { name ->
                                    backStack.add(AppRoutes.GenericEditRoute(name))
                                }
                            )
                        }
                    }

                    is AppRoutes.ZonesPlanRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            com.example.clicker.ui.screens.festivalEdit.ZonesPlanScreen(
                                festivalId = key.festivalId,
                                onNavigateToEdit = { name ->
                                    backStack.add(AppRoutes.GenericEditRoute(name))
                                }
                            )
                        }
                    }

                    is AppRoutes.GenericEditRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            com.example.clicker.ui.screens.festivalEdit.GenericEditScreen(
                                screenTitle = key.screenTitle,
                                onBackClick = {
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                    }

                    else -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            Text("Erreur")
                        }
                    }
                }
            }
        )
    }
}

private fun isDestinationSelected(currentDestination: Any?, destination: Destination): Boolean {
    return when (destination) {
        Destination.JEUX -> currentDestination == Destination.JEUX ||
                currentDestination == AppRoutes.GameCreateRoute ||
                currentDestination is AppRoutes.GameDetailRoute ||
                currentDestination is AppRoutes.GameEditRoute

        Destination.FESTIVALS -> currentDestination == Destination.FESTIVALS ||
                currentDestination == AppRoutes.FestivalCreateRoute ||
                currentDestination is AppRoutes.FestivalDetailRoute ||
                currentDestination is AppRoutes.FestivalModifRoute ||
                currentDestination is AppRoutes.ModifDetailRoute ||
                currentDestination is AppRoutes.StockTablesRoute ||
                currentDestination is AppRoutes.StockMaterielRoute ||
                currentDestination is AppRoutes.ZonesTarifRoute ||
                currentDestination is AppRoutes.ZonesPlanRoute ||
                currentDestination is AppRoutes.GenericEditRoute

        else -> currentDestination == destination
    }
}
