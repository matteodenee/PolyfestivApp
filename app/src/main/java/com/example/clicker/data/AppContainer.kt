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
import com.example.clicker.data.reservation.ReservationsRepository
import com.example.clicker.data.reservationGame.ReservationGameRepository
import com.example.clicker.data.reservationTariffzoneAllocation.ReservationTariffzoneAllocationRepository
import com.example.clicker.data.tarifZone.TarifZoneRepository
import com.example.clicker.data.reservationNote.ReservationNoteRepository
import com.example.clicker.data.reservationContact.ReservationContactRepository
import com.example.clicker.data.invoice.InvoiceRepository
import com.example.clicker.data.equipment.EquipmentRepository

interface AppContainer {
    val authRepository: AuthRepository
    val gamesRepository: GamesRepository
    val festivalsRepository: FestivalsRepository
    val tablesRepository: TablesRepository
    val equipmentsRepository: EquipmentsRepository
    val tariffZonesRepository: TariffZonesRepository
    val mapZonesRepository: MapZonesRepository
    val adminRepository: AdminRepository
    val exposantRepository: ExposantRepository
    val reservationsRepository: ReservationsRepository
    val reservationTariffzoneAllocationRepository: ReservationTariffzoneAllocationRepository
    val reservationGameRepository: ReservationGameRepository
    val reservationNoteRepository: ReservationNoteRepository
    val reservationContactRepository: ReservationContactRepository
    val invoiceRepository: InvoiceRepository
}

