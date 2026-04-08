package com.example.clicker.data

import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.festival.FestivalsRepository
import com.example.clicker.data.table.TablesRepository
import com.example.clicker.data.equipment.EquipmentsRepository
import com.example.clicker.data.zone.TariffZonesRepository
import com.example.clicker.data.zone.MapZonesRepository
import com.example.clicker.data.admin.AdminRepository

interface AppContainer {
    val authRepository: AuthRepository
    val gamesRepository: GamesRepository
    val festivalsRepository: FestivalsRepository
    val tablesRepository: TablesRepository
    val equipmentsRepository: EquipmentsRepository
    val tariffZonesRepository: TariffZonesRepository
    val mapZonesRepository: MapZonesRepository
}
    val adminRepository: AdminRepository
    val exposantRepository: ExposantRepository
}
