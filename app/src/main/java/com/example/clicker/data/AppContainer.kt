package com.example.clicker.data

import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.admin.AdminRepository
import com.example.clicker.data.equipment.EquipmentRepository
import com.example.clicker.data.mapZone.MapZoneRepository
import com.example.clicker.data.festivalTable.FestivalTableRepository
import com.example.clicker.data.festivalEquipmentStock.FestivalEquipmentStockRepository
import com.example.clicker.data.reservationGamePlacement.ReservationGamePlacementRepository

interface AppContainer {
    val authRepository: AuthRepository
    val gamesRepository: GamesRepository
    val adminRepository: AdminRepository
    val exposantRepository: ExposantRepository
    val equipmentRepository: EquipmentRepository
    val mapZoneRepository: MapZoneRepository
    val festivalTableRepository: FestivalTableRepository
    val festivalEquipmentStockRepository: FestivalEquipmentStockRepository
    val reservationGamePlacementRepository: ReservationGamePlacementRepository
}