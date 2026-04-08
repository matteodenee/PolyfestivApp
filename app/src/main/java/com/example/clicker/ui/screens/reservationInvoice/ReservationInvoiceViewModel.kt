package com.example.clicker.ui.screens.reservationInvoice

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.equipment.EquipmentsRepository
import com.example.clicker.data.invoice.InvoiceDto
import com.example.clicker.data.invoice.InvoiceRepository
import com.example.clicker.data.invoice.InvoiceRequest
import com.example.clicker.data.reservation.ReservationDto
import com.example.clicker.data.reservationGame.ReservationGameRepository
import com.example.clicker.data.reservationTariffzoneAllocation.ReservationTariffzoneAllocationRepository
import com.example.clicker.data.zone.TariffZonesRepository
import kotlinx.coroutines.launch

class ReservationInvoiceViewModel(
    private val invoiceRepository: InvoiceRepository,
    private val tarifZoneRepository: TariffZonesRepository,
    private val allocationRepository: ReservationTariffzoneAllocationRepository,
    private val reservationGameRepository: ReservationGameRepository,
    private val equipmentRepository: EquipmentsRepository
) : ViewModel() {

    private val internalState = mutableStateOf<ReservationInvoiceUiState>(ReservationInvoiceUiState.Loading)
    val state: State<ReservationInvoiceUiState> = internalState

    fun loadInvoiceData(reservation: ReservationDto) {
        viewModelScope.launch {
            internalState.value = ReservationInvoiceUiState.Loading
            try {
                val invoices = invoiceRepository.getInvoicesByReservation(reservation.id)
                val tariffZones = tarifZoneRepository.getTariffZonesByFestival(reservation.festivalId)
                val allocations = allocationRepository.getAllocationsByReservation(reservation.id)
                val reservationGames = reservationGameRepository.getGamesByReservation(reservation.id)
                val equipments = equipmentRepository.getEquipmentsByFestival(reservation.festivalId)

                val tablesCost = allocations.sumOf { allocation ->
                    val zone = tariffZones.firstOrNull { it.id == allocation.tariffzoneId }
                    allocation.quantityTables.toDouble() * (zone?.tableprice ?: 0.0)
                }

                val totalChairsRequested = reservationGames.sumOf { it.chairsNeeded ?: 0 }
                val totalOutletsRequested = reservationGames.sumOf { it.outletsNeeded ?: 0 }

                val chairUnitPrice =
                    equipments.firstOrNull { it.kind == "CHAIR" }?.unitPrice ?: 0.0

                val outletUnitPrice =
                    equipments.firstOrNull { it.kind == "ELECTRIC_OUTLET" }?.unitPrice ?: 0.0

                val chairsCost = totalChairsRequested * chairUnitPrice
                val electricCost = totalOutletsRequested * outletUnitPrice

                val totalPrice = tablesCost + chairsCost + electricCost

                internalState.value = ReservationInvoiceUiState.Success(
                    invoices = invoices,
                    tablesCost = tablesCost,
                    chairsCost = chairsCost,
                    electricCost = electricCost,
                    totalPrice = totalPrice
                )
            } catch (e: Exception) {
                internalState.value = ReservationInvoiceUiState.Error(
                    e.message ?: "Impossible de charger la facture"
                )
            }
        }
    }

    fun createInvoiceIfNeeded(reservation: ReservationDto) {
        val currentState = internalState.value as? ReservationInvoiceUiState.Success ?: return

        viewModelScope.launch {
            try {
                invoiceRepository.createInvoice(
                    InvoiceRequest(
                        reservationId = reservation.id,
                        amountTtc = currentState.totalPrice,
                        vatRate = 20.0
                    )
                )
                loadInvoiceData(reservation)
            } catch (e: Exception) {
                internalState.value = ReservationInvoiceUiState.Error(
                    e.message ?: "Impossible de créer la facture"
                )
            }
        }
    }

    fun markPaid(invoice: InvoiceDto, reservation: ReservationDto) {
        viewModelScope.launch {
            try {
                invoiceRepository.markInvoicePaid(invoice.id)
                loadInvoiceData(reservation)
            } catch (e: Exception) {
                internalState.value = ReservationInvoiceUiState.Error(
                    e.message ?: "Impossible de marquer la facture comme payée"
                )
            }
        }
    }
}