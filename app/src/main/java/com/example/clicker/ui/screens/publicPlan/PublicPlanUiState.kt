package com.example.clicker.ui.screens.publicPlan

import com.example.clicker.data.festival.FestivalDto

sealed interface PublicPlanUiState {
    data object Loading : PublicPlanUiState
    data class Error(val message: String) : PublicPlanUiState
    data class Success(
        val festivals: List<FestivalDto>,
        val currentFestivalId: Int,
        val zones: List<PublicZoneUi>,
        val unplacedGames: List<PublicPlacementUi>,
        val publishers: List<PublicPublisherUi>
    ) : PublicPlanUiState
}

data class PublicZoneUi(
    val id: Int,
    val name: String,
    val tariffzoneLabel: String,
    val capacityTables: Int,
    val surface: Double,
    val placements: List<PublicPlacementUi>
)

data class PublicPlacementUi(
    val id: Int,
    val reservationId: Int,
    val reservantName: String,
    val gameName: String,
    val tableType: String,
    val tablesAllocated: Int,
    val chairsAllocated: Int,
    val outletsAllocated: Int
)

data class PublicPublisherUi(
    val reservantId: Int,
    val name: String,
    val actorTypes: List<String>,
    val games: List<String>
)