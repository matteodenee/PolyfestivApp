package com.example.clicker.ui.navigation

import com.example.clicker.data.reservation.ReservationDto

sealed interface AppRoutes {
    data object LOGIN : AppRoutes
    data object REGISTER : AppRoutes

    data object GameCreateRoute : AppRoutes
    data class GameDetailRoute(val gameId: Int) : AppRoutes
    data class GameEditRoute(val gameId: Int) : AppRoutes

    data object ExposantCreateRoute : AppRoutes
    data class ExposantDetailRoute(val exposantId: Int) : AppRoutes
    data class ExposantEditRoute(val exposantId: Int) : AppRoutes
    data class ReservationsRoute(val festivalId: Int) : AppRoutes
    data class ReservationCreateRoute(val festivalId: Int) : AppRoutes
    data class ReservationDetailRoute(val reservation: ReservationDto) : AppRoutes
    data class ReservationSuppliesRoute(val reservation: ReservationDto) : AppRoutes
}