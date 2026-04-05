package com.example.clicker.data

import android.content.Context
import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.local.game.GameDatabase
import com.example.clicker.data.network.RetrofitInstance
import com.example.clicker.data.admin.AdminRepository
import com.example.clicker.data.equipment.EquipmentRepository
import com.example.clicker.data.mapZone.MapZoneRepository
import com.example.clicker.data.festivalTable.FestivalTableRepository
import com.example.clicker.data.festivalEquipmentStock.FestivalEquipmentStockRepository
import com.example.clicker.data.reservationGamePlacement.ReservationGamePlacementRepository

class AppDataContainer(
    private val context: Context
) : AppContainer {

    override val authRepository: AuthRepository by lazy {
        AuthRepository(RetrofitInstance.authApi)
    }

    override val exposantRepository: ExposantRepository by lazy {
        ExposantRepository(RetrofitInstance.exposantApi)
    }

    override val gamesRepository: GamesRepository by lazy {
        GamesRepository(
            api = RetrofitInstance.gamesApi,
            gameDao = GameDatabase.getDatabase(context).gameDao()
        )
    }

    override val adminRepository: AdminRepository by lazy {
        AdminRepository(RetrofitInstance.adminApi)
    }

    override val equipmentRepository: EquipmentRepository by lazy {
        EquipmentRepository(RetrofitInstance.equipmentApi)
    }
    override val mapZoneRepository: MapZoneRepository by lazy {
        MapZoneRepository(RetrofitInstance.mapZoneApi)
    }

    override val festivalTableRepository: FestivalTableRepository by lazy {
        FestivalTableRepository(RetrofitInstance.festivalTableApi)
    }

    override val festivalEquipmentStockRepository: FestivalEquipmentStockRepository by lazy {
        FestivalEquipmentStockRepository(RetrofitInstance.festivalEquipmentStockApi)
    }

    override val reservationGamePlacementRepository: ReservationGamePlacementRepository by lazy {
        ReservationGamePlacementRepository(RetrofitInstance.reservationGamePlacementApi)
    }
}