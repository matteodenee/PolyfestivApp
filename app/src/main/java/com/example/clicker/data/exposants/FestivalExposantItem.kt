package com.example.clicker.data.exposants

data class FestivalExposantItem(
    val exposant: Exposant,
    val contacted: Boolean,
    val status: String? = null,
)