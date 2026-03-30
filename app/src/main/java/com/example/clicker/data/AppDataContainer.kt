package com.example.clicker.data

import android.content.Context
import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.local.game.GameDatabase
import com.example.clicker.data.network.RetrofitInstance
import com.example.clicker.data.admin.AdminRepository
import com.example.clicker.data.reservation.ReservationsRepository
import com.example.clicker.data.reservationGame.ReservationGameRepository
import com.example.clicker.data.reservationTariffzoneAllocation.ReservationTariffzoneAllocationRepository
import com.example.clicker.data.tarifZone.TarifZoneRepository

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

    override val reservationsRepository: ReservationsRepository by lazy {
        ReservationsRepository(RetrofitInstance.reservationsApi)
    }

    override val reservationTariffzoneAllocationRepository: ReservationTariffzoneAllocationRepository by lazy {
        ReservationTariffzoneAllocationRepository(RetrofitInstance.reservationTariffzoneAllocationApi)
    }

    override val reservationGameRepository: ReservationGameRepository by lazy {
        ReservationGameRepository(RetrofitInstance.reservationGameApi)
    }

    override val tarifZoneRepository: TarifZoneRepository by lazy {
        TarifZoneRepository(RetrofitInstance.tarifZoneApi)
    }
}