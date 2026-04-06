package com.example.clicker.ui.navigation

sealed interface AppRoutes {
    data object LOGIN : AppRoutes
    data object REGISTER : AppRoutes
    data object GameCreateRoute : AppRoutes
    data class GameDetailRoute(val gameId: Int) : AppRoutes
    data class GameEditRoute(val gameId: Int) : AppRoutes
    data object FestivalListRoute : AppRoutes
    data class FestivalDetailRoute(val festivalId: Int) : AppRoutes
    data class FestivalModifRoute(val festivalId: Int) : AppRoutes
    data class ModifDetailRoute(val festivalId: Int) : AppRoutes
    data class StockTablesRoute(val festivalId: Int) : AppRoutes
    data class StockMaterielRoute(val festivalId: Int) : AppRoutes
    data class ZonesTarifRoute(val festivalId: Int) : AppRoutes
    data class ZonesPlanRoute(val festivalId: Int) : AppRoutes
    
    // Generic edit route, uses screenTitle and fields based on what it's editing
    data class GenericEditRoute(val screenTitle: String) : AppRoutes
}