package com.example.clicker

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.clicker.ui.navigation.ClickerNavHost

@Composable
fun ClickerApp(
    navController: NavHostController = rememberNavController()
) {
    ClickerNavHost(navController = navController)
}