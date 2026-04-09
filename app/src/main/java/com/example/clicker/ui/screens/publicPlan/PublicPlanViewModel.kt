package com.example.clicker.ui.screens.publicPlan

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.data.festival.FestivalsRepository
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.mapZone.MapZoneRepository
import com.example.clicker.data.reservation.ReservationsRepository
import com.example.clicker.data.reservationGame.ReservationGameRepository
import com.example.clicker.data.reservationGamePlacement.ReservationGamePlacementRepository
import com.example.clicker.data.zone.TariffZonesRepository
import kotlinx.coroutines.launch

class PublicPlanViewModel(
    private val festivalsRepository: FestivalsRepository,
    private val mapZoneRepository: MapZoneRepository,
    private val reservationsRepository: ReservationsRepository,
    private val reservationGameRepository: ReservationGameRepository,
    private val reservationGamePlacementRepository: ReservationGamePlacementRepository,
    private val tarifZoneRepository: TariffZonesRepository,
    private val gamesRepository: GamesRepository,
    private val exposantRepository: ExposantRepository
) : ViewModel() {

    private val internalState = mutableStateOf<PublicPlanUiState>(PublicPlanUiState.Loading)
    val state: State<PublicPlanUiState> = internalState

    private var currentFestivalId by mutableStateOf<Int?>(null)

    fun loadInitial(festivalId: Int) {
        currentFestivalId = festivalId
        loadFestival(festivalId)
    }

    fun selectFestival(festivalId: Int) {
        currentFestivalId = festivalId
        loadFestival(festivalId)
    }

    private fun loadFestival(festivalId: Int) {
        viewModelScope.launch {
            internalState.value = PublicPlanUiState.Loading

            try {
                val festivals = festivalsRepository.getFestivals().sortedBy { it.name.lowercase() }
                val mapZones = mapZoneRepository.getMapZonesByFestival(festivalId)
                val reservations = reservationsRepository.getReservationsByFestival(festivalId)
                val placements = reservationGamePlacementRepository.getPlacementsByFestival(festivalId)
                val tariffZones = tarifZoneRepository.getTariffZonesByFestival(festivalId)
                val games = gamesRepository.getGames()
                val exposants = exposantRepository.getExposants()

                // Pour chaque réservation, on récupère les jeux réservés
                val reservationGames = reservations.flatMap { reservation ->
                    reservationGameRepository.getGamesByReservation(reservation.id)
                        .map { reservation.id to it }
                }

                // Dictionnaires pour retrouver un objet à partir de son id
                val gamesById = games.associateBy { it.id }
                val reservationsById = reservations.associateBy { it.id }
                val exposantsById = exposants.associateBy { it.id }
                val tariffZonesById = tariffZones.associateBy { it.id }

                // Transformation des placements en objets adaptés à l’écran public
                val publicPlacements = placements.map { placement ->
                    val reservation = reservationsById[placement.reservationId]
                    val reservantName = reservation?.reservantId?.let { exposantsById[it]?.name }
                        ?: "Réservation #${placement.reservationId}"

                    PublicPlacementUi(
                        id = placement.id,
                        reservationId = placement.reservationId,
                        reservantName = reservantName,
                        gameName = gamesById[placement.gameId]?.name ?: "Jeu #${placement.gameId}",
                        tableType = placement.tableType,
                        tablesAllocated = placement.tablesAllocated,
                        chairsAllocated = placement.chairsAllocated ?: 0,
                        outletsAllocated = placement.outletsAllocated ?: 0
                    )
                }

                // On regroupe les placements par id de zone
                val placementsByZoneId = placements
                    .filter { it.mapzoneId != null }
                    .groupBy { it.mapzoneId!! }

                val publicPlacementsById = publicPlacements.associateBy { it.id }

                // Construction des zones
                val zones = mapZones
                    .sortedBy { it.name.lowercase() }
                    .map { zone ->
                        val zonePlacements = placementsByZoneId[zone.id]
                            .orEmpty()
                            .mapNotNull { publicPlacementsById[it.id] }

                        PublicZoneUi(
                            id = zone.id,
                            name = zone.name,
                            tariffzoneLabel = tariffZonesById[zone.tariffzoneid]?.let {
                                "${it.name} (${it.tableprice}€ la table)"
                            } ?: "Zone tarifaire #${zone.tariffzoneid}",
                            capacityTables = zone.nbtable,
                            surface = zone.surface,
                            placements = zonePlacements
                        )
                    }


                // On crée une clé pour tous les jeux déjà placés
                val placedKeys = placements
                    .map { "${it.reservationId}-${it.gameId}" }
                    .toSet()

                // Jeux non placés, on part de tous les jeux réservés, puis on garde uniquement ceux qui n’ont pas de clé dans placedKeys
                val unplacedGames = reservationGames
                    .filter { (reservationId, reservationGame) ->
                        "${reservationId}-${reservationGame.gameId}" !in placedKeys
                    }
                    .map { (reservationId, reservationGame) ->
                        val reservation = reservationsById[reservationId]
                        val reservantName = reservation?.reservantId?.let { exposantsById[it]?.name }
                            ?: "Réservation #$reservationId"

                        PublicPlacementUi(
                            id = -1,
                            reservationId = reservationId,
                            reservantName = reservantName,
                            gameName = gamesById[reservationGame.gameId]?.name ?: "Jeu #${reservationGame.gameId}",
                            tableType = "-",
                            tablesAllocated = reservationGame.tablesNeeded ?: 0,
                            chairsAllocated = reservationGame.chairsNeeded ?: 0,
                            outletsAllocated = reservationGame.outletsNeeded ?: 0
                        )
                    }

                // liste exposants
                val publishers = placements
                    .mapNotNull { placement ->
                        val reservation = reservationsById[placement.reservationId] ?: return@mapNotNull null
                        val exposant = exposantsById[reservation.reservantId] ?: return@mapNotNull null
                        val gameName = gamesById[placement.gameId]?.name ?: "Jeu #${placement.gameId}"
                        Triple(exposant.id, exposant, gameName)
                    }
                    .groupBy { it.first }
                    .values
                    .map { entries ->
                        val exposant = entries.first().second
                        PublicPublisherUi(
                            reservantId = exposant.id,
                            name = exposant.name,
                            actorTypes = exposant.actorType,
                            games = entries.map { it.third }.distinct().sorted()
                        )
                    }
                    .sortedBy { it.name.lowercase() }

                internalState.value = PublicPlanUiState.Success(
                    festivals = festivals,
                    currentFestivalId = currentFestivalId ?: festivalId,
                    zones = zones,
                    unplacedGames = unplacedGames,
                    publishers = publishers
                )
            } catch (e: Exception) {
                internalState.value = PublicPlanUiState.Error(
                    e.message ?: "Impossible de charger le plan public"
                )
            }
        }
    }
}