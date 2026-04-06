package com.example.clicker.data

import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.festival.FestivalsRepository
import com.example.clicker.data.table.TablesRepository
import com.example.clicker.data.equipment.EquipmentsRepository
import com.example.clicker.data.zone.TariffZonesRepository
import com.example.clicker.data.zone.MapZonesRepository
import com.example.clicker.data.network.RetrofitInstance


class AppDataContainer : AppContainer {

    override val authRepository: AuthRepository by lazy {
        AuthRepository(RetrofitInstance.authApi)
    }

    override val gamesRepository: GamesRepository by lazy {
        GamesRepository(RetrofitInstance.gamesApi)
    }

    override val festivalsRepository: FestivalsRepository by lazy {
        FestivalsRepository(RetrofitInstance.festivalsApi)
    }

    override val tablesRepository: TablesRepository by lazy {
        TablesRepository(RetrofitInstance.tablesApi)
    }

    override val equipmentsRepository: EquipmentsRepository by lazy {
        EquipmentsRepository(RetrofitInstance.equipmentsApi)
    }

    override val tariffZonesRepository: TariffZonesRepository by lazy {
        TariffZonesRepository(RetrofitInstance.tariffZonesApi)
    }

    override val mapZonesRepository: MapZonesRepository by lazy {
        MapZonesRepository(RetrofitInstance.mapZonesApi)
    }
}
