package com.example.clicker.ui.navigation

sealed interface AppRoutes {
    data object LOGIN : AppRoutes
    data object REGISTER : AppRoutes
    data object ExposantCreateRoute : AppRoutes
    data class ExposantDetailRoute(val exposantId: Int) : AppRoutes
    data class ExposantEditRoute(val exposantId: Int) : AppRoutes
}