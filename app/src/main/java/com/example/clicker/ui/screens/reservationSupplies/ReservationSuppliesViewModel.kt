package com.example.clicker.ui.screens.reservationSupplies

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.reservation.ReservationDto
import com.example.clicker.data.reservation.ReservationRequest
import com.example.clicker.data.reservation.ReservationsRepository
import com.example.clicker.data.reservationGame.ReservationGameRepository
import com.example.clicker.data.reservationGame.ReservationGameRequest
import com.example.clicker.data.reservationTariffzoneAllocation.ReservationTariffzoneAllocationRepository
import com.example.clicker.data.reservationTariffzoneAllocation.ReservationTariffzoneAllocationRequest
import com.example.clicker.data.tarifZone.TarifZoneRepository
import kotlinx.coroutines.launch

class ReservationSuppliesViewModel(
    private val tarifZoneRepository: TarifZoneRepository,
    private val allocationRepository: ReservationTariffzoneAllocationRepository,
    private val reservationGameRepository: ReservationGameRepository,
    private val gamesRepository: GamesRepository,
    private val reservationsRepository: ReservationsRepository
) : ViewModel() {

    private val internalState = mutableStateOf<ReservationSuppliesUiState>(ReservationSuppliesUiState.Loading)
    val state: State<ReservationSuppliesUiState> = internalState

    var selectedTariffZoneId by mutableStateOf<Int?>(null)
        private set

    var tableCountInput by mutableStateOf("") // pour table hors jeux
        private set

    var selectedGameId by mutableStateOf<Int?>(null)
        private set

    var gameTablesInput by mutableStateOf("") // pour table avec jeux
        private set

    var gameChairsInput by mutableStateOf("")
        private set

    var gameOutletsInput by mutableStateOf("")
        private set

    val gameNamesById = mutableStateMapOf<Int, String>()

    fun loadSupplies(reservation: ReservationDto) {
        viewModelScope.launch {
            internalState.value = ReservationSuppliesUiState.Loading
            try {
                // On recharge toutes les réservations du festival depuis le backend
                val refreshedReservation =
                    reservationsRepository
                        .getReservationsByFestival(reservation.festivalId)
                        .firstOrNull { it.id == reservation.id }// On cherche dans la liste la réservation qui correspond à celle affichée
                        ?: reservation // Si on ne la trouve pas (null), on garde l'ancienne réservation pour éviter un crash

                val tariffZones =
                    tarifZoneRepository.getTarifZonesByFestival(refreshedReservation.festivalId)

                val allocations =
                    allocationRepository.getAllocationsByReservation(refreshedReservation.id)

                val reservationGames =
                    reservationGameRepository.getGamesByReservation(refreshedReservation.id)

                val games = gamesRepository.getGames()
                gameNamesById.clear()
                gameNamesById.putAll(games.associate { it.id to it.name })

                internalState.value = ReservationSuppliesUiState.Success(
                    reservation = refreshedReservation,
                    tariffZones = tariffZones,
                    allocations = allocations,
                    reservationGames = reservationGames
                )
            } catch (e: Exception) {
                internalState.value = ReservationSuppliesUiState.Error(
                    e.message ?: "Impossible de charger les fournitures"
                )
            }
        }
    }

    fun onTariffZoneChange(tariffZoneId: Int) {
        selectedTariffZoneId = tariffZoneId
    }

    fun onTableCountChange(value: String) {
        tableCountInput = value.filter { it.isDigit() }
    }

    fun onGameChange(gameId: Int) {
        selectedGameId = gameId
    }

    fun onGameTablesChange(value: String) {
        gameTablesInput = value.filter { it.isDigit() }
    }

    fun onGameChairsChange(value: String) {
        gameChairsInput = value.filter { it.isDigit() }
    }

    fun onGameOutletsChange(value: String) {
        gameOutletsInput = value.filter { it.isDigit() }
    }

    fun addAllocation(reservation: ReservationDto) {
        val tariffZoneId = selectedTariffZoneId ?: return
        val quantityTablesToAdd = tableCountInput.toIntOrNull() ?: return
        if (quantityTablesToAdd <= 0) return

        viewModelScope.launch {
            try {
                val currentState = internalState.value as? ReservationSuppliesUiState.Success
                val existingAllocation = currentState
                    ?.allocations
                    ?.firstOrNull { it.tariffzoneId == tariffZoneId }

                if (existingAllocation == null) {
                    allocationRepository.createAllocation(
                        ReservationTariffzoneAllocationRequest(
                            reservationId = reservation.id,
                            tariffzoneId = tariffZoneId,
                            quantityTables = quantityTablesToAdd,
                            quantityAreaSqm = 0
                        )
                    )
                } else {
                    allocationRepository.updateAllocation(
                        id = existingAllocation.id,
                        request = ReservationTariffzoneAllocationRequest(
                            reservationId = existingAllocation.reservationId,
                            tariffzoneId = existingAllocation.tariffzoneId,
                            quantityTables = existingAllocation.quantityTables + quantityTablesToAdd,
                            quantityAreaSqm = existingAllocation.quantityAreaSqm
                        )
                    )
                }

                selectedTariffZoneId = null
                tableCountInput = ""
                loadSupplies(reservation)
            } catch (e: Exception) {
                internalState.value = ReservationSuppliesUiState.Error(
                    e.message ?: "Impossible d'ajouter les tables"
                )
            }
        }
    }

    fun addReservationGame(reservation: ReservationDto) {
        val gameId = selectedGameId ?: return

        val tablesToAdd = gameTablesInput.toIntOrNull() ?: 0
        val chairsToAdd = gameChairsInput.toIntOrNull() ?: 0
        val outletsToAdd = gameOutletsInput.toIntOrNull() ?: 0

        if (tablesToAdd <= 0 && chairsToAdd <= 0 && outletsToAdd <= 0) return

        viewModelScope.launch {
            try {
                val currentState = internalState.value as? ReservationSuppliesUiState.Success
                val existingGame = currentState
                    ?.reservationGames
                    ?.firstOrNull { it.gameId == gameId }

                if (existingGame == null) {
                    reservationGameRepository.createReservationGame(
                        ReservationGameRequest(
                            reservationId = reservation.id,
                            gameId = gameId,
                            tablesNeeded = tablesToAdd,
                            chairsNeeded = chairsToAdd,
                            outletsNeeded = outletsToAdd
                        )
                    )
                } else {
                    reservationGameRepository.updateReservationGame(
                        id = existingGame.id,
                        request = ReservationGameRequest(
                            reservationId = existingGame.reservationId,
                            gameId = existingGame.gameId,
                            tablesNeeded = (existingGame.tablesNeeded ?: 0) + tablesToAdd,
                            chairsNeeded = (existingGame.chairsNeeded ?: 0) + chairsToAdd,
                            outletsNeeded = (existingGame.outletsNeeded ?: 0) + outletsToAdd
                        )
                    )
                }

                selectedGameId = null
                gameTablesInput = ""
                gameChairsInput = ""
                gameOutletsInput = ""
                loadSupplies(reservation)
            } catch (e: Exception) {
                internalState.value = ReservationSuppliesUiState.Error(
                    e.message ?: "Impossible d'ajouter le jeu"
                )
            }
        }
    }

    fun deleteAllocation(allocationId: Int, reservation: ReservationDto) {
        viewModelScope.launch {
            try {
                allocationRepository.deleteAllocation(allocationId)
                loadSupplies(reservation)
            } catch (e: Exception) {
                internalState.value = ReservationSuppliesUiState.Error(
                    e.message ?: "Impossible de supprimer cette ligne"
                )
            }
        }
    }

    fun deleteReservationGame(reservationGameId: Int, reservation: ReservationDto) {
        viewModelScope.launch {
            try {
                reservationGameRepository.deleteReservationGame(reservationGameId)
                loadSupplies(reservation)
            } catch (e: Exception) {
                internalState.value = ReservationSuppliesUiState.Error(
                    e.message ?: "Impossible de supprimer ce jeu"
                )
            }
        }
    }

    fun updateTracking(
        reservation: ReservationDto,
        presentsGames: Boolean = reservation.presentsGames,
        gamesListRequested: Boolean = reservation.gamesListRequested,
        gamesListReceived: Boolean = reservation.gamesListReceived,
        gamesReceived: Boolean = reservation.gamesReceived
    ) {
        viewModelScope.launch {
            try {
                val updated = reservationsRepository.updateReservation(
                    id = reservation.id,
                    request = ReservationRequest(
                        id = reservation.id,
                        festivalId = reservation.festivalId,
                        reservantId = reservation.reservantId,
                        status = reservation.status,
                        priceBeforeDiscount = reservation.priceBeforeDiscount,
                        discountAmount = reservation.discountAmount,
                        totalPrice = reservation.totalPrice,
                        freeTables = reservation.freeTables,
                        presentsGames = presentsGames,
                        gamesListRequested = gamesListRequested,
                        gamesListReceived = gamesListReceived,
                        gamesReceived = gamesReceived
                    )
                )

                val currentState = internalState.value as? ReservationSuppliesUiState.Success
                if (currentState != null) {
                    internalState.value = currentState.copy(reservation = updated)
                } else {
                    loadSupplies(updated)
                }
            } catch (e: Exception) {
                internalState.value = ReservationSuppliesUiState.Error(
                    e.message ?: "Impossible de mettre à jour le suivi"
                )
            }
        }
    }
}