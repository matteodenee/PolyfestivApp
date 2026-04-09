package com.example.clicker.data.invoice

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceRequest(
    val reservationId: Int? = null,
    val amountTtc: Double? = null,
    val vatRate: Double? = null,
    val status: String? = null,
    val issuedAt: String? = null,
    val dueDate: String? = null
)