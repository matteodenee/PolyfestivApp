package com.example.clicker.data

import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.exposants.ExposantRepository
import com.example.clicker.data.game.GamesRepository
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
    val adminRepository: AdminRepository
    val exposantRepository: ExposantRepository
    val reservationsRepository: ReservationsRepository
    val reservationTariffzoneAllocationRepository: ReservationTariffzoneAllocationRepository
    val reservationGameRepository: ReservationGameRepository
    val tarifZoneRepository: TarifZoneRepository
    val reservationNoteRepository: ReservationNoteRepository
    val reservationContactRepository: ReservationContactRepository
    val invoiceRepository: InvoiceRepository
    val equipmentRepository: EquipmentRepository
}