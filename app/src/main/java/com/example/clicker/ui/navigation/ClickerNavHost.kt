package com.example.clicker.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.example.clicker.ui.screens.exposants.DetailsExposantScreen
import com.example.clicker.ui.screens.exposants.ExposantFormMode
import com.example.clicker.ui.screens.exposants.ExposantFormScreen
import com.example.clicker.ui.screens.exposants.ExposantViewModel
import com.example.clicker.ui.screens.exposants.ExposantsScreen
import com.example.clicker.ui.screens.login.LoginScreen
import com.example.clicker.ui.screens.register.RegisterScreen
import com.example.clicker.ui.theme.PrimaryYellow
import com.example.clicker.ui.viewmodel.AppViewModelProvider

sealed interface ExposantNavKey {
    data object Form : ExposantNavKey
    data class Details(val id: Int) : ExposantNavKey
    data class Edit(val id: Int) : ExposantNavKey
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickerNavHost() {
    val backStack = remember { mutableStateListOf<Any>(AppRoutes.LOGIN) }
    val currentDestination = backStack.lastOrNull()
    val showBars = currentDestination is Destination

    val exposantViewModel: ExposantViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
    val exposantUiState by exposantViewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (showBars) {
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
            if (showBars) {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Destination.entries.forEach { destination ->
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
                            Text("Festival")
                        }
                    }

                    Destination.JEUX -> NavEntry(key) {
                        Box(modifier = Modifier.padding(innerPadding)) {
                            Text("Jeux")
                        }
                    }

                    Destination.EXPOSANTS -> NavEntry(key) {
                        ExposantsScreen(
                            modifier = Modifier.padding(innerPadding),
                            exposantViewModel = exposantViewModel,
                            onAddClick = {
                                backStack.add(ExposantNavKey.Form)
                            },
                            onDetailsClick = { exposantId ->
                                backStack.add(ExposantNavKey.Details(exposantId))
                            }
                        )
                    }

                    ExposantNavKey.Form -> NavEntry(key) {
                        ExposantFormScreen(
                            mode = ExposantFormMode.CREATE,
                            exposant = null,
                            onBackClick = {
                                backStack.removeLastOrNull()
                            },
                            onSaveClick = { formData ->
                                exposantViewModel.saveNewExposant(formData)
                                backStack.removeLastOrNull()
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    is ExposantNavKey.Details -> NavEntry(key) {
                        val exposant = exposantUiState.findExposantById(key.id)

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
                                            backStack.add(ExposantNavKey.Edit(key.id))
                                        },
                                        onDeleteClick = {
                                            exposantViewModel.deleteExposantById(key.id)
                                            backStack.removeLastOrNull()
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }

                    is ExposantNavKey.Edit -> NavEntry(key) {
                        val exposant = exposantUiState.findExposantById(key.id)

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
                                            exposantViewModel.updateExposant(key.id, formData)
                                            backStack.removeLastOrNull()
                                        },
                                        modifier = Modifier.padding(innerPadding)
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