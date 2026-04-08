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
import com.example.clicker.ui.screens.festivalDetail.FestivalDetailScreen
import com.example.clicker.ui.screens.festivalDetail.FestivalModifScreen
import com.example.clicker.ui.screens.festivalList.FestivalCreateScreen
import com.example.clicker.ui.screens.festivalList.FestivalScreen
import com.example.clicker.ui.screens.gameCreate.GameCreateScreen
import com.example.clicker.ui.screens.gameDetail.GameDetailScreen
import com.example.clicker.ui.screens.gameEdit.GameEditScreen
import com.example.clicker.ui.screens.games.GamesScreen
import com.example.clicker.ui.screens.login.LoginScreen
import com.example.clicker.ui.screens.login.LoginViewModel
import com.example.clicker.ui.screens.register.RegisterScreen
import com.example.clicker.ui.screens.reservationContact.ReservationContactScreen
import com.example.clicker.ui.screens.reservationCreate.ReservationCreateScreen
import com.example.clicker.ui.screens.reservationDetail.ReservationDetailScreen
import com.example.clicker.ui.screens.reservationInvoice.ReservationInvoiceScreen
import com.example.clicker.ui.screens.reservationNote.ReservationNoteScreen
import com.example.clicker.ui.screens.reservationSupplies.ReservationSuppliesScreen
import com.example.clicker.ui.screens.reservations.ReservationsScreen
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
    var festivalsRefreshKey by remember { mutableIntStateOf(0) }
    var detailRefreshKey by remember { mutableIntStateOf(0) }
    var adminRefreshKey by remember { mutableIntStateOf(0) }
    var reservationRefreshKey by remember { mutableIntStateOf(0) }

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
                                AppRoutes.FestivalCreateRoute -> "Nouveau Festival"
                                is AppRoutes.FestivalDetailRoute -> "Détail Festival"
                                is AppRoutes.FestivalModifRoute -> "Modification"
                                is AppRoutes.ModifDetailRoute -> "Détail festival"
                                is AppRoutes.StockTablesRoute -> "Stock tables"
                                is AppRoutes.StockMaterielRoute -> "Stock matériel"
                                is AppRoutes.ZonesTarifRoute -> "Zones tarifaires"
                                is AppRoutes.ZonesPlanRoute -> "Zones du plan"
                                is AppRoutes.GenericEditRoute -> currentDestination.screenTitle.replace("Plan ", "")
                                is AppRoutes.ReservationsRoute -> "Réservations"
                                is AppRoutes.ReservationCreateRoute -> "Nouvelle réservation"
                                is AppRoutes.ReservationDetailRoute -> "Détail"
                                is AppRoutes.ReservationSuppliesRoute -> "Fournitures"
                                is AppRoutes.ReservationContactRoute -> "Prise de contact"
                                is AppRoutes.ReservationNoteRoute -> "Note"
                                is AppRoutes.ReservationInvoiceRoute -> "Facture"
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

                    AppRoutes.FestivalCreateRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            FestivalCreateScreen(
                                onCreateSuccess = { festivalId ->
                                    festivalsRefreshKey++
                                    backStack.removeLastOrNull()
                                    backStack.add(AppRoutes.FestivalDetailRoute(festivalId))
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
                                onReservationsClick = { festivalId ->
                                    backStack.add(AppRoutes.ReservationsRoute(festivalId))
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

                    is AppRoutes.ReservationsRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            ReservationsScreen(
                                festivalId = key.festivalId,
                                refreshKey = reservationRefreshKey,
                                onAddClick = {
                                    backStack.add(AppRoutes.ReservationCreateRoute(key.festivalId))
                                },
                                onReservationClick = { reservation ->
                                    backStack.add(AppRoutes.ReservationDetailRoute(reservation))
                                }
                            )
                        }
                    }

                    is AppRoutes.ReservationCreateRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            ReservationCreateScreen(
                                festivalId = key.festivalId,
                                onCreateSuccess = {
                                    reservationRefreshKey++
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                    }

                    is AppRoutes.ReservationDetailRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            ReservationDetailScreen(
                                reservation = key.reservation,
                                onDeleteSuccess = {
                                    reservationRefreshKey++
                                    backStack.removeLastOrNull()
                                },
                                onSuppliesClick = { _, _ ->
                                    backStack.add(AppRoutes.ReservationSuppliesRoute(key.reservation))
                                },
                                onInvoiceClick = {
                                    backStack.add(AppRoutes.ReservationInvoiceRoute(key.reservation))
                                },
                                onContactClick = {
                                    backStack.add(AppRoutes.ReservationContactRoute(key.reservation))
                                },
                                onNoteClick = {
                                    backStack.add(AppRoutes.ReservationNoteRoute(key.reservation))
                                }
                            )
                        }
                    }

                    is AppRoutes.ReservationSuppliesRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            ReservationSuppliesScreen(
                                reservation = key.reservation
                            )
                        }
                    }

                    is AppRoutes.ReservationContactRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            ReservationContactScreen(
                                reservation = key.reservation
                            )
                        }
                    }

                    is AppRoutes.ReservationNoteRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            ReservationNoteScreen(
                                reservation = key.reservation
                            )
                        }
                    }

                    is AppRoutes.ReservationInvoiceRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            ReservationInvoiceScreen(
                                reservation = key.reservation
                            )
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

                    Destination.EXPOSANTS -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            ExposantsScreen(
                                exposantViewModel = exposantViewModel,
                                onAddClick = {
                                    backStack.add(AppRoutes.ExposantCreateRoute)
                                },
                                onDetailsClick = { exposantId ->
                                    backStack.add(AppRoutes.ExposantDetailRoute(exposantId))
                                }
                            )
                        }
                    }

                    AppRoutes.ExposantCreateRoute -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            ExposantFormScreen(
                                mode = ExposantFormMode.CREATE,
                                exposant = null,
                                onBackClick = {
                                    backStack.removeLastOrNull()
                                },
                                onSaveClick = { formData ->
                                    exposantViewModel.saveNewExposant(formData)
                                    backStack.removeLastOrNull()
                                }
                            )
                        }
                    }

                    is AppRoutes.ExposantDetailRoute -> NavEntry(key) {
                        val exposant = exposantUiState.findExposantById(key.exposantId)

                        Box(modifier = Modifier.padding(innerPadding)) {
                            when {
                                exposantUiState.isLoading -> {
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
                                            exposantViewModel.deleteExposantById(key.exposantId)
                                            backStack.removeLastOrNull()
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }

                    is AppRoutes.ExposantEditRoute -> NavEntry(key) {
                        val exposant = exposantUiState.findExposantById(key.exposantId)

                        Box(modifier = Modifier.padding(innerPadding)) {
                            when {
                                exposantUiState.isLoading -> {
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
                                            exposantViewModel.updateExposant(key.exposantId, formData)
                                            backStack.removeLastOrNull()
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Destination.ADMIN -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            AdminScreen(
                                refreshKey = adminRefreshKey
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
        Destination.FESTIVALS ->
            currentDestination == Destination.FESTIVALS ||
                currentDestination == AppRoutes.FestivalCreateRoute ||
                currentDestination is AppRoutes.FestivalDetailRoute ||
                currentDestination is AppRoutes.FestivalModifRoute ||
                currentDestination is AppRoutes.ModifDetailRoute ||
                currentDestination is AppRoutes.StockTablesRoute ||
                currentDestination is AppRoutes.StockMaterielRoute ||
                currentDestination is AppRoutes.ZonesTarifRoute ||
                currentDestination is AppRoutes.ZonesPlanRoute ||
                currentDestination is AppRoutes.GenericEditRoute ||
                currentDestination is AppRoutes.ReservationsRoute ||
                currentDestination is AppRoutes.ReservationCreateRoute ||
                currentDestination is AppRoutes.ReservationDetailRoute ||
                currentDestination is AppRoutes.ReservationSuppliesRoute ||
                currentDestination is AppRoutes.ReservationContactRoute ||
                currentDestination is AppRoutes.ReservationNoteRoute ||
                currentDestination is AppRoutes.ReservationInvoiceRoute

        Destination.JEUX ->
            currentDestination == Destination.JEUX ||
                currentDestination == AppRoutes.GameCreateRoute ||
                currentDestination is AppRoutes.GameDetailRoute ||
                currentDestination is AppRoutes.GameEditRoute

        Destination.EXPOSANTS ->
            currentDestination == Destination.EXPOSANTS ||
                currentDestination == AppRoutes.ExposantCreateRoute ||
                currentDestination is AppRoutes.ExposantDetailRoute ||
                currentDestination is AppRoutes.ExposantEditRoute

        Destination.ADMIN -> currentDestination == Destination.ADMIN
    }
}