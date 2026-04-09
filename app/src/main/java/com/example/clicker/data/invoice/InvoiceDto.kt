package com.example.clicker.data.invoice

import kotlinx.serialization.Serializable

@Serializable
data class InvoiceDto(
    val id: Int,
    val reservationId: Int,
    val number: String,
    val amountTtc: Double,
    val vatRate: Double,
    val issuedAt: String? = null,
    val dueDate: String? = null,
    val status: String
)