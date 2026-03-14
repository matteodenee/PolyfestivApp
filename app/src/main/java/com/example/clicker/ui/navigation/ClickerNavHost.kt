package com.example.clicker.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clicker.ui.screens.MainScreen
import com.example.clicker.ui.screens.login.LoginScreen

@Composable
fun ClickerNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        composable(route = LoginDestination.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main") {
                        popUpTo(LoginDestination.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = "main") {
            MainScreen()
        }
    }
}