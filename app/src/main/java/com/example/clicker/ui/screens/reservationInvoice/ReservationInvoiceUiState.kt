package com.example.clicker.ui.screens.reservationInvoice

import com.example.clicker.data.invoice.InvoiceDto

sealed interface ReservationInvoiceUiState {
    data object Loading : ReservationInvoiceUiState

    data class Success(
        val invoices: List<InvoiceDto>,
        val tablesCost: Double,
        val chairsCost: Double,
        val electricCost: Double,
        val totalPrice: Double
    ) : ReservationInvoiceUiState

    data class Error(val message: String) : ReservationInvoiceUiState
}