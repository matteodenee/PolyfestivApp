package com.example.clicker.ui.navigation

import com.example.clicker.data.reservation.ReservationDto

sealed interface AppRoutes {
    data object LOGIN : AppRoutes
    data object REGISTER : AppRoutes

    data object GameCreateRoute : AppRoutes
    data class GameDetailRoute(val gameId: Int) : AppRoutes
    data class GameEditRoute(val gameId: Int) : AppRoutes
    data object FestivalListRoute : AppRoutes
    data object FestivalCreateRoute : AppRoutes
    data class FestivalDetailRoute(val festivalId: Int) : AppRoutes
    data class FestivalModifRoute(val festivalId: Int) : AppRoutes
    data class ModifDetailRoute(val festivalId: Int) : AppRoutes
    data class StockTablesRoute(val festivalId: Int) : AppRoutes
    data class StockMaterielRoute(val festivalId: Int) : AppRoutes
    data class ZonesTarifRoute(val festivalId: Int) : AppRoutes
    data class ZonesPlanRoute(val festivalId: Int) : AppRoutes
    
    // Generic edit route, uses screenTitle and fields based on what it's editing
    data class GenericEditRoute(val screenTitle: String) : AppRoutes

    data object ExposantCreateRoute : AppRoutes
    data class ExposantDetailRoute(val exposantId: Int) : AppRoutes
    data class ExposantEditRoute(val exposantId: Int) : AppRoutes
    data class PublicPlanRoute(val festivalId: Int) : AppRoutes
    data class ReservationPlacementRoute(val festivalId: Int) : AppRoutes
    data class ReservationsRoute(val festivalId: Int) : AppRoutes
    data class ReservationCreateRoute(val festivalId: Int) : AppRoutes
    data class ReservationDetailRoute(val reservation: ReservationDto) : AppRoutes
    data class ReservationSuppliesRoute(val reservation: ReservationDto) : AppRoutes
    data class ReservationContactRoute(val reservation: ReservationDto) : AppRoutes
    data class ReservationNoteRoute(val reservation: ReservationDto) : AppRoutes
    data class ReservationInvoiceRoute(val reservation: ReservationDto) : AppRoutes
}