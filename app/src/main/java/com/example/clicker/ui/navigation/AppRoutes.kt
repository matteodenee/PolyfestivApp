package com.example.clicker.ui.navigation

sealed interface AppRoutes {
    data object LOGIN : AppRoutes
    data object REGISTER : AppRoutes
    data object GameCreateRoute : AppRoutes
    data class GameDetailRoute(val gameId: Int) : AppRoutes
    data class GameEditRoute(val gameId: Int) : AppRoutes
    data object FestivalListRoute : AppRoutes
    data class FestivalDetailRoute(val festivalId: Int) : AppRoutes
}