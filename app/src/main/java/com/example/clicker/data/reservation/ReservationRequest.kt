package com.example.clicker.data.reservation

import kotlinx.serialization.Serializable

@Serializable
data class ReservationRequest(
    val id: Int? = null,
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