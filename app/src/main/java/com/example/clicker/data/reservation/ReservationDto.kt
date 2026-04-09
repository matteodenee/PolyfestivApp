package com.example.clicker.data.reservation

import kotlinx.serialization.Serializable

@Serializable
data class ReservationDto(
    val id: Int,
    val festivalId: Int,
    val reservantId: Int,
    val status: Int = 0,
    val priceBeforeDiscount: Double? = null,
    val discountAmount: Double? = null,
    val totalPrice: Double? = null,
    val freeTables: Int? = null,
    val presentsGames: Boolean = false,
    val gamesListRequested: Boolean = false,
    val gamesListReceived: Boolean = false,
    val gamesReceived: Boolean = false
)

fun ReservationDto.statusLabel(): String {
    return when (status) {
        0 -> "Pas encore contacté"
        1 -> "Contact pris"
        2 -> "Discussion"
        3 -> "Sera absent"
        4 -> "Considéré absent"
        5 -> "Confirmé"
        6 -> "Facturé"
        7 -> "Payé"
        else -> "Inconnu"
    }
}