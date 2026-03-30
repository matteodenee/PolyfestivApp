package com.example.clicker.ui.navigation

sealed interface AppRoutes {
    data object LOGIN : AppRoutes
    data object REGISTER : AppRoutes

    data object GameCreateRoute : AppRoutes
    data class GameDetailRoute(val gameId: Int) : AppRoutes
    data class GameEditRoute(val gameId: Int) : AppRoutes

    data object ExposantCreateRoute : AppRoutes
    data class ExposantDetailRoute(val exposantId: Int) : AppRoutes
    data class ExposantEditRoute(val exposantId: Int) : AppRoutes

    data class FestivalGamesRoute(
        val festivalId: Int,
        val festivalName: String? = null
    ) : AppRoutes

    data class FestivalExposantsRoute(
        val festivalId: Int,
        val festivalName: String? = null
    ) : AppRoutes
}