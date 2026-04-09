package com.example.clicker.ui.screens.reservationPlacement

import com.example.clicker.data.mapZone.MapZoneDto
import com.example.clicker.data.reservationGame.ReservationGameDto
import com.example.clicker.data.reservationGamePlacement.ReservationGamePlacementDto

sealed interface ReservationPlacementUiState {
    data object Loading : ReservationPlacementUiState

    data class Success(
        val summary: PlacementSummaryUi,
        val zones: List<PlacementZoneUi>,
        val unplacedGames: List<UnplacedGameUi>,
        val availableTableTypes: List<String>
    ) : ReservationPlacementUiState

    data class Error(val message: String) : ReservationPlacementUiState
}

data class PlacementSummaryUi(
    val totalGames: Int,
    val placedGames: Int,
    val waitingGames: Int,
    val tablesUsed: Int,
    val tablesRemaining: Int,
    val tablesCapacity: Int
)

data class PlacementZoneUi(
    val mapZone: MapZoneDto,
    val tariffZoneName: String,
    val placedGames: List<PlacedGameUi>
)

data class PlacedGameUi(
    val placement: ReservationGamePlacementDto,
    val reservationId: Int,
    val gameName: String,
    val tablesText: String
)

data class UnplacedGameUi(
    val reservationId: Int,
    val reservationGame: ReservationGameDto,
    val gameName: String,
    val tablesNeeded: Int,
    val chairsNeeded: Int,
    val outletsNeeded: Int
)