package com.example.clicker.ui.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.clicker.ui.screens.MainScreen
import com.example.clicker.ui.screens.login.LoginScreen
import com.example.clicker.ui.screens.register.RegisterScreen

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
                },
                onNavigateToRegister = {
                    navController.navigate(RegisterDestination.route)
                }
            )
        }
        composable(route = RegisterDestination.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(RegisterDestination.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(RegisterDestination.route) { inclusive = true }
                    }
                }
            )
        }

        composable(route = "main") {
            MainScreen()
        }
    }
}