package com.example.clicker.ui.screens.reservationPlacement

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.equipment.EquipmentRepository
import com.example.clicker.data.festivalEquipmentStock.FestivalEquipmentStockRepository
import com.example.clicker.data.festivalTable.FestivalTableRepository
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.mapZone.MapZoneRepository
import com.example.clicker.data.reservation.ReservationsRepository
import com.example.clicker.data.reservationGame.ReservationGameRepository
import com.example.clicker.data.reservationGamePlacement.ReservationGamePlacementRepository
import com.example.clicker.data.reservationGamePlacement.ReservationGamePlacementRequest
import com.example.clicker.data.reservationTariffzoneAllocation.ReservationTariffzoneAllocationDto
import com.example.clicker.data.reservationTariffzoneAllocation.ReservationTariffzoneAllocationRepository
import com.example.clicker.data.reservationTariffzoneAllocation.ReservationTariffzoneAllocationRequest
import com.example.clicker.data.tarifZone.TarifZoneRepository
import kotlinx.coroutines.launch
import kotlin.math.ceil
import retrofit2.HttpException
import android.util.Log

class ReservationPlacementViewModel(
    private val reservationsRepository: ReservationsRepository,
    private val reservationGameRepository: ReservationGameRepository,
    private val reservationGamePlacementRepository: ReservationGamePlacementRepository,
    private val reservationTariffzoneAllocationRepository: ReservationTariffzoneAllocationRepository,
    private val tarifZoneRepository: TarifZoneRepository,
    private val mapZoneRepository: MapZoneRepository,
    private val festivalTableRepository: FestivalTableRepository,
    private val equipmentRepository: EquipmentRepository,
    private val festivalEquipmentStockRepository: FestivalEquipmentStockRepository,
    private val gamesRepository: GamesRepository
) : ViewModel() {

    private val internalState = mutableStateOf<ReservationPlacementUiState>(ReservationPlacementUiState.Loading)
    val state: State<ReservationPlacementUiState> = internalState

    var selectedMapZoneId by mutableStateOf<Int?>(null)
        private set

    val selectedTableTypeByReservationGameKey = mutableStateMapOf<String, String>()

    var actionErrorMessage by mutableStateOf<String?>(null)
        private set

    fun loadPlacementData(festivalId: Int) {
        viewModelScope.launch {
            internalState.value = ReservationPlacementUiState.Loading
            actionErrorMessage = null

            try {
                val reservations = reservationsRepository.getReservationsByFestival(festivalId)

                val reservationGames = reservations.flatMap { reservation ->
                    reservationGameRepository.getGamesByReservation(reservation.id)
                        .map { game -> reservation.id to game }
                }

                val placements = reservationGamePlacementRepository.getPlacementsByFestival(festivalId)

                val allocations = reservations.flatMap { reservation ->
                    reservationTariffzoneAllocationRepository.getAllocationsByReservation(reservation.id)
                }

                val tariffZones = tarifZoneRepository.getTarifZonesByFestival(festivalId)

                val mapZones = mapZoneRepository.getMapZonesByFestival(festivalId)

                val festivalTables = festivalTableRepository.getTablesByFestival(festivalId)

                val games = gamesRepository.getGames()
                val gameNamesById = games.associate { it.id to it.name }

                val totalFestivalTables = festivalTables.sumOf { it.quantity }
                val placedTables = placements.sumOf { it.tablesAllocated }
                val totalTablesAlreadyUsed = placedTables

                val unplacedGamesUi = reservationGames
                    .filter { (reservationId, game) ->
                        placements.none {
                            it.reservationId == reservationId && it.gameId == game.gameId
                        }
                    }
                    .map { (reservationId, game) ->
                        UnplacedGameUi(
                            reservationId = reservationId,
                            reservationGame = game,
                            gameName = gameNamesById[game.gameId] ?: "Jeu #${game.gameId}",
                            tablesNeeded = game.tablesNeeded ?: 0,
                            chairsNeeded = game.chairsNeeded ?: 0,
                            outletsNeeded = game.outletsNeeded ?: 0
                        )
                    }

                val zonesUi = mapZones
                    .sortedBy { it.name.lowercase() }
                    .map { zone ->
                        val placedGames = placements
                            .filter { it.mapzoneId == zone.id }
                            .map { placement ->
                                val linkedGame = reservationGames.firstOrNull {
                                    it.first == placement.reservationId &&
                                            it.second.gameId == placement.gameId
                                }?.second

                                val tables = placement.tablesAllocated
                                val chairs = placement.chairsAllocated ?: linkedGame?.chairsNeeded ?: 0
                                val outlets = placement.outletsAllocated ?: linkedGame?.outletsNeeded ?: 0

                                PlacedGameUi(
                                    placement = placement,
                                    reservationId = placement.reservationId,
                                    gameName = gameNamesById[placement.gameId] ?: "Jeu #${placement.gameId}",
                                    tablesText = "Réservation #${placement.reservationId} • ${tables} table(s) (${placement.tableType}) • ${chairs} chaise(s) • ${outlets} prise(s)"
                                )
                            }

                        PlacementZoneUi(
                            mapZone = zone,
                            tariffZoneName = tariffZones.firstOrNull { it.id == zone.tariffzoneid }?.name
                                ?: "Zone tarifaire #${zone.tariffzoneid}",
                            placedGames = placedGames
                        )
                    }

                val availableTableTypes = festivalTables
                    .mapNotNull { it.type?.trim() }
                    .filter { it.isNotEmpty() }
                    .distinct()

                internalState.value = ReservationPlacementUiState.Success(
                    summary = PlacementSummaryUi(
                        totalGames = reservationGames.size,
                        placedGames = placements.size,
                        waitingGames = unplacedGamesUi.size,
                        tablesUsed = totalTablesAlreadyUsed,
                        tablesRemaining = (totalFestivalTables - totalTablesAlreadyUsed).coerceAtLeast(0),
                        tablesCapacity = totalFestivalTables
                    ),
                    zones = zonesUi,
                    unplacedGames = unplacedGamesUi,
                    availableTableTypes = availableTableTypes
                )
            } catch (e: Exception) {
                internalState.value = ReservationPlacementUiState.Error(
                    e.message ?: "Impossible de charger le placement des jeux"
                )
            }
        }
    }

    fun selectMapZone(mapZoneId: Int) {
        selectedMapZoneId = mapZoneId
        actionErrorMessage = null
    }

    fun onTableTypeChange(reservationId: Int, gameId: Int, tableType: String) {
        selectedTableTypeByReservationGameKey[reservationGameKey(reservationId, gameId)] =
            tableType.trim()
        actionErrorMessage = null
    }

    fun placeGame(festivalId: Int, game: UnplacedGameUi) {
        val selectedZoneId = selectedMapZoneId
        val selectedTableType =
            selectedTableTypeByReservationGameKey[
                reservationGameKey(game.reservationId, game.reservationGame.gameId)
            ]?.trim()

        if (selectedZoneId == null) {
            actionErrorMessage = "Sélectionne d’abord une zone du plan."
            return
        }

        if (selectedTableType.isNullOrBlank()) {
            actionErrorMessage = "Choisis un type de table pour ce jeu."
            return
        }

        viewModelScope.launch {
            try {
                actionErrorMessage = null

                val mapZones = mapZoneRepository.getMapZonesByFestival(festivalId)
                val selectedZone = mapZones.firstOrNull { it.id == selectedZoneId }

                if (selectedZone == null) {
                    actionErrorMessage = "Zone introuvable."
                    return@launch
                }

                val allPlacements = reservationGamePlacementRepository.getPlacementsByFestival(festivalId)
                val reservationPlacements = allPlacements.filter { it.reservationId == game.reservationId
                }

                val reservationAllocations = reservationTariffzoneAllocationRepository.getAllocationsByReservation(game.reservationId)
                val festivalTables = festivalTableRepository.getTablesByFestival(festivalId)
                val equipments = equipmentRepository.getEquipmentsByFestival(festivalId)
                val equipmentStocks = festivalEquipmentStockRepository.getStocksByFestival(festivalId)

                val tablesNeeded = game.tablesNeeded
                val chairsNeeded = game.chairsNeeded
                val outletsNeeded = game.outletsNeeded

                // Capacité en tables dans la zone
                val zoneUsedTables = allPlacements
                    .filter { it.mapzoneId == selectedZone.id }
                    .sumOf { it.tablesAllocated }

                if (zoneUsedTables + tablesNeeded > selectedZone.nbtable) {
                    actionErrorMessage = "Capacité de la zone du plan dépassée (tables)"
                    return@launch
                }

                // Capacité surface dans la zone
                val zoneSurface = selectedZone.surface
                if (zoneSurface > 0.0) {
                    val tableAreaM2 = 4.0
                    val usedArea = zoneUsedTables.toDouble() * tableAreaM2
                    val nextArea = usedArea + tablesNeeded.toDouble() * tableAreaM2

                    if (nextArea > zoneSurface) {
                        actionErrorMessage = "Capacité de la zone du plan dépassée (surface)"
                        return@launch
                    }
                }

                // Stock festival pour ce type de table exact
                val stockForSelectedType = festivalTables
                    .filter { it.type.trim() == selectedTableType }
                    .sumOf { it.quantity }

                val usedTablesOfThisType = allPlacements
                    .filter { it.tableType.trim() == selectedTableType }
                    .sumOf { it.tablesAllocated }

                if (usedTablesOfThisType + tablesNeeded > stockForSelectedType) {
                    actionErrorMessage = "Stock de tables insuffisant pour ce type"
                    return@launch
                }

                // Chaises / prises
                val chairEquipmentId = equipments.firstOrNull {
                    it.kind.trim().equals("CHAIR", ignoreCase = true)
                }?.id

                val outletEquipmentId = equipments.firstOrNull {
                    it.kind.trim().equals("ELECTRIC_OUTLET", ignoreCase = true)
                }?.id

                val availableChairStock = equipmentStocks.firstOrNull {
                    it.equipmentId == chairEquipmentId
                }?.quantityAvailable ?: Int.MAX_VALUE

                val availableOutletStock = equipmentStocks.firstOrNull {
                    it.equipmentId == outletEquipmentId
                }?.quantityAvailable ?: Int.MAX_VALUE

                if (chairsNeeded > availableChairStock) {
                    actionErrorMessage = "Stock de chaises insuffisant."
                    return@launch
                }

                if (outletsNeeded > availableOutletStock) {
                    actionErrorMessage = "Stock de prises insuffisant."
                    return@launch
                }

                ensureTariffzoneAllocationForPlacement(
                    reservationId = game.reservationId,
                    tariffzoneId = selectedZone.tariffzoneid,
                    tablesNeeded = tablesNeeded,
                    reservationPlacements = reservationPlacements,
                    mapZones = mapZones,
                    reservationAllocations = reservationAllocations
                )

                reservationGamePlacementRepository.createPlacement(
                    ReservationGamePlacementRequest(
                        reservationId = game.reservationId,
                        gameId = game.reservationGame.gameId,
                        tablesAllocated = tablesNeeded,
                        tableType = selectedTableType,
                        chairsAllocated = chairsNeeded,
                        outletsAllocated = outletsNeeded,
                        mapzoneId = selectedZoneId
                    )
                )

                selectedTableTypeByReservationGameKey.remove(
                    reservationGameKey(game.reservationId, game.reservationGame.gameId)
                )

                loadPlacementData(festivalId)
            } catch (e: HttpException) {
                actionErrorMessage =
                    extractApiErrorMessage(e) ?: "Erreur HTTP ${e.code()} pendant le placement."
            } catch (e: Exception) {
                actionErrorMessage = e.message ?: "Impossible de placer ce jeu."
            }
        }
    }

    private suspend fun ensureTariffzoneAllocationForPlacement( //
        reservationId: Int,
        tariffzoneId: Int,
        tablesNeeded: Int,
        reservationPlacements: List<com.example.clicker.data.reservationGamePlacement.ReservationGamePlacementDto>,
        mapZones: List<com.example.clicker.data.mapZone.MapZoneDto>,
        reservationAllocations: List<ReservationTariffzoneAllocationDto>
    ) {
        val bookedTablesForTariff = reservationAllocations
            .filter { it.tariffzoneId == tariffzoneId }
            .sumOf { it.quantityTables.toDouble() + (it.quantityAreaSqm.toDouble() / 4.0) }

        val mapZoneIdsOfSameTariff = mapZones
            .filter { it.tariffzoneid == tariffzoneId }
            .map { it.id }

        val alreadyPlacedForTariff = reservationPlacements
            .filter { it.mapzoneId in mapZoneIdsOfSameTariff }
            .sumOf { it.tablesAllocated.toDouble() }

        val missingTables = ceil(
            (alreadyPlacedForTariff + tablesNeeded.toDouble()) - bookedTablesForTariff
        ).toInt().coerceAtLeast(0)

        Log.d(
            "PLACEMENT_DEBUG",
            "zoneTarif=$tariffzoneId | booked=$bookedTablesForTariff | already=$alreadyPlacedForTariff | needed=$tablesNeeded | missing=$missingTables"
        )

        if (missingTables <= 0) return

        val existingAllocation = reservationAllocations.firstOrNull { it.tariffzoneId == tariffzoneId }

        if (existingAllocation != null) {
            reservationTariffzoneAllocationRepository.updateAllocation(
                id = existingAllocation.id,
                request = ReservationTariffzoneAllocationRequest(
                    reservationId = reservationId,
                    tariffzoneId = tariffzoneId,
                    quantityTables = existingAllocation.quantityTables + missingTables,
                    quantityAreaSqm = existingAllocation.quantityAreaSqm
                )
            )
        } else {
            reservationTariffzoneAllocationRepository.createAllocation(
                ReservationTariffzoneAllocationRequest(
                    reservationId = reservationId,
                    tariffzoneId = tariffzoneId,
                    quantityTables = missingTables,
                    quantityAreaSqm = 0
                )
            )
        }
    }

    fun removePlacement(festivalId: Int, placementId: Int) {
        viewModelScope.launch {
            try {
                actionErrorMessage = null
                reservationGamePlacementRepository.deletePlacement(placementId)
                loadPlacementData(festivalId)
            } catch (e: Exception) {
                actionErrorMessage = e.message ?: "Impossible de retirer ce placement."
            }
        }
    }

    private fun reservationGameKey(reservationId: Int, gameId: Int): String {
        return "$reservationId-$gameId"
    }

    private fun extractApiErrorMessage(e: HttpException): String? {
        return try {
            e.response()?.errorBody()?.string()
        } catch (_: Exception) {
            null
        }
    }
}