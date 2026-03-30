package com.example.clicker.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.clicker.ui.screens.admin.AdminScreen
import com.example.clicker.ui.screens.exposants.DetailsExposantScreen
import com.example.clicker.ui.screens.exposants.ExposantFormMode
import com.example.clicker.ui.screens.exposants.ExposantFormScreen
import com.example.clicker.ui.screens.exposants.ExposantViewModel
import com.example.clicker.ui.screens.exposants.ExposantsScreen
import com.example.clicker.ui.screens.gameCreate.GameCreateScreen
import com.example.clicker.ui.screens.gameDetail.GameDetailScreen
import com.example.clicker.ui.screens.gameEdit.GameEditScreen
import com.example.clicker.ui.screens.games.GamesScreen
import com.example.clicker.ui.screens.login.LoginScreen
import com.example.clicker.ui.screens.login.LoginViewModel
import com.example.clicker.ui.screens.register.RegisterScreen
import com.example.clicker.ui.theme.PrimaryYellow
import com.example.clicker.ui.utils.network.NetworkUtils
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickerNavHost(
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory),
    exposantViewModel: ExposantViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val backStack = remember { mutableStateListOf<Any>(AppRoutes.LOGIN) }
    val exposantUiState by exposantViewModel.uiState.collectAsState()

    var gamesRefreshKey by remember { mutableIntStateOf(0) }
    var detailRefreshKey by remember { mutableIntStateOf(0) }
    var adminRefreshKey by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        loginViewModel.restoreSessionIfNeeded {
            backStack.clear()
            backStack.add(Destination.FESTIVALS)
        }
    }

    val currentDestination = backStack.lastOrNull()
    val shouldDisplayBars = currentDestination !in listOf(AppRoutes.LOGIN, AppRoutes.REGISTER)
    val isAdmin = loginViewModel.isAdmin()

    val bottomDestinations = if (isAdmin) {
        Destination.entries.toList()
    } else {
        Destination.entries.filter { it != Destination.ADMIN }
    }

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
                                Destination.ADMIN -> "Admin"
                                AppRoutes.GameCreateRoute,
                                AppRoutes.ExposantCreateRoute -> "Ajout"
                                is AppRoutes.GameDetailRoute,
                                is AppRoutes.ExposantDetailRoute -> "Détail"
                                is AppRoutes.GameEditRoute,
                                is AppRoutes.ExposantEditRoute -> "Modification"
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
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                loginViewModel.logout {
                                    backStack.clear()
                                    backStack.add(AppRoutes.LOGIN)
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Logout,
                                contentDescription = "Déconnexion"
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
                        bottomDestinations.forEach { destination ->
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
                                    adminRefreshKey++
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
                            Text("Festival")
                        }
                    }

                    Destination.JEUX -> NavEntry(key) {
                        val isOnline = NetworkUtils.isInternetAvailable(context)

                        Box(modifier = Modifier.padding(innerPadding)) {
                            GamesScreen(
                                refreshKey = gamesRefreshKey,
                                canAdd = isOnline,
                                onGameClick = { gameId ->
                                    backStack.add(AppRoutes.GameDetailRoute(gameId))
                                },
                                onAddClick = {
                                    if (NetworkUtils.isInternetAvailable(context)) {
                                        backStack.add(AppRoutes.GameCreateRoute)
                                    }
                                }
                            )
                        }
                    }

                    Destination.EXPOSANTS -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            ExposantsScreen(
                                exposantViewModel = exposantViewModel,
                                onAddClick = {
                                    val isOnline =
                                        (exposantUiState as? com.example.clicker.ui.screens.exposants.ExposantUiState.Success)?.isOnline == true

                                    if (isOnline) {
                                        backStack.add(AppRoutes.ExposantCreateRoute)
                                    }
                                },
                                onDetailsClick = { exposantId ->
                                    backStack.add(AppRoutes.ExposantDetailRoute(exposantId))
                                }
                            )
                        }
                    }

                    Destination.ADMIN -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            AdminScreen(
                                refreshKey = adminRefreshKey
                            )
                        }
                    }

                    AppRoutes.GameCreateRoute -> NavEntry(key) {
                        val isOnline = NetworkUtils.isInternetAvailable(context)

                        if (!isOnline) {
                            LaunchedEffect(Unit) {
                                backStack.removeLastOrNull()
                            }

                            Box(modifier = Modifier.padding(innerPadding)) {
                                Text("Connexion internet requise")
                            }
                        } else {
                            Box(modifier = Modifier.padding(innerPadding)) {
                                GameCreateScreen(
                                    onCreateSuccess = {
                                        gamesRefreshKey++
                                        backStack.removeLastOrNull()
                                    }
                                )
                            }
                        }
                    }

                    is AppRoutes.GameDetailRoute -> NavEntry(key) {
                        val isOnline = NetworkUtils.isInternetAvailable(context)

                        Box(modifier = Modifier.padding(innerPadding)) {
                            GameDetailScreen(
                                gameId = key.gameId,
                                refreshKey = detailRefreshKey,
                                canDelete = isOnline,
                                canEdit = isOnline,
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
                        val isOnline = NetworkUtils.isInternetAvailable(context)

                        if (!isOnline) {
                            LaunchedEffect(Unit) {
                                backStack.removeLastOrNull()
                            }

                            Box(modifier = Modifier.padding(innerPadding)) {
                                Text("Connexion internet requise")
                            }
                        } else {
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
                    }

                    AppRoutes.ExposantCreateRoute -> NavEntry(key) {
                        val isOnline =
                            (exposantUiState as? com.example.clicker.ui.screens.exposants.ExposantUiState.Success)?.isOnline == true

                        Box(modifier = Modifier.padding(innerPadding)) {
                            ExposantFormScreen(
                                mode = ExposantFormMode.CREATE,
                                exposant = null,
                                onBackClick = {
                                    backStack.removeLastOrNull()
                                },
                                onSaveClick = { formData ->
                                    exposantViewModel.saveNewExposant(formData) {
                                        backStack.removeLastOrNull()
                                    }
                                },
                                isOnline = isOnline
                            )
                        }
                    }

                    is AppRoutes.ExposantDetailRoute -> NavEntry(key) {
                        val exposantState = exposantUiState as? com.example.clicker.ui.screens.exposants.ExposantUiState.Success
                        val exposant = exposantState?.exposants?.find { it.id == key.exposantId }
                        val isOnline = exposantState?.isOnline == true

                        Box(modifier = Modifier.padding(innerPadding)) {
                            when {
                                exposantUiState is com.example.clicker.ui.screens.exposants.ExposantUiState.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }

                                exposant == null -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Exposant introuvable")
                                    }
                                }

                                else -> {
                                    DetailsExposantScreen(
                                        exposant = exposant,
                                        onBackClick = {
                                            backStack.removeLastOrNull()
                                        },
                                        onEditClick = {
                                            backStack.add(AppRoutes.ExposantEditRoute(key.exposantId))
                                        },
                                        onDeleteClick = {
                                            exposantViewModel.deleteExposantById(key.exposantId) {
                                                backStack.removeLastOrNull()
                                            }
                                        },
                                        isOnline = isOnline,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }

                    is AppRoutes.ExposantEditRoute -> NavEntry(key) {
                        val exposantState = exposantUiState as? com.example.clicker.ui.screens.exposants.ExposantUiState.Success
                        val exposant = exposantState?.exposants?.find { it.id == key.exposantId }
                        val isOnline = exposantState?.isOnline == true

                        Box(modifier = Modifier.padding(innerPadding)) {
                            when {
                                exposantUiState is com.example.clicker.ui.screens.exposants.ExposantUiState.Loading -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                }

                                exposant == null -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text("Exposant introuvable")
                                    }
                                }

                                else -> {
                                    ExposantFormScreen(
                                        mode = ExposantFormMode.EDIT,
                                        exposant = exposant,
                                        onBackClick = {
                                            backStack.removeLastOrNull()
                                        },
                                        onSaveClick = { formData ->
                                            exposantViewModel.updateExposant(key.exposantId, formData) {
                                                backStack.removeLastOrNull()
                                            }
                                        },
                                        isOnline = isOnline
                                    )
                                }
                            }
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

        Destination.EXPOSANTS -> currentDestination == Destination.EXPOSANTS ||
                currentDestination == AppRoutes.ExposantCreateRoute ||
                currentDestination is AppRoutes.ExposantDetailRoute ||
                currentDestination is AppRoutes.ExposantEditRoute

        else -> currentDestination == destination
    }
}