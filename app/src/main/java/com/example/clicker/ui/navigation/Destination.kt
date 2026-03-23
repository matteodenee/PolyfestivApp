package com.example.clicker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.AdminPanelSettings

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    FESTIVALS("festivals", "Festivals", Icons.Default.Celebration, "Festivals"),
    JEUX("jeux", "Jeux", Icons.Default.Casino, "Jeux"),
    EXPOSANTS("exposants", "Exposants", Icons.Default.Storefront, "Exposants"),
    ADMIN("admin", "Admin", Icons.Default.AdminPanelSettings, "Admin")
}