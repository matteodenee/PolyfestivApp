package com.example.clicker.data

import android.content.Context
import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.data.game.GamesRepository
import com.example.clicker.data.festival.FestivalsRepository
import com.example.clicker.data.table.TablesRepository
import com.example.clicker.data.equipment.EquipmentsRepository
import com.example.clicker.data.zone.TariffZonesRepository
import com.example.clicker.data.zone.MapZonesRepository
import com.example.clicker.data.local.game.GameDatabase
import com.example.clicker.data.network.RetrofitInstance
import com.example.clicker.data.admin.AdminRepository
import com.example.clicker.data.reservation.ReservationsRepository
import com.example.clicker.data.reservationGame.ReservationGameRepository
import com.example.clicker.data.reservationTariffzoneAllocation.ReservationTariffzoneAllocationRepository
import com.example.clicker.data.reservationNote.ReservationNoteRepository
import com.example.clicker.data.reservationContact.ReservationContactRepository
import com.example.clicker.data.invoice.InvoiceRepository

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

    override val reservationNoteRepository: ReservationNoteRepository by lazy {
        ReservationNoteRepository(
            RetrofitInstance.reservationNoteApi
        )
    }

    override val reservationContactRepository: ReservationContactRepository by lazy {
        ReservationContactRepository(RetrofitInstance.reservationContactApi)
    }
    override val invoiceRepository: InvoiceRepository by lazy {
        InvoiceRepository(RetrofitInstance.invoiceApi)
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

