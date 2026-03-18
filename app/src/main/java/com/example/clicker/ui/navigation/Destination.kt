package com.example.clicker.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.ui.graphics.vector.ImageVector

enum class Destination(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val contentDescription: String
) {
    FESTIVALS(
        route = "festivals",
        label = "Festivals",
        icon = Icons.Default.Celebration,
        contentDescription = "Festivals"
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