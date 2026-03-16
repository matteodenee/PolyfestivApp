package com.example.clicker.data.model

data class Exposant(
    val id: Int,
    val name: String,
    val actorType: List<String>,
    val email: String? = null,
    val phone: String? = null,
    val description: String? = null,
    val reservantType: String? = null,
    val billingAddress: String? = null,
)

data class ExposantFormData(
    val name: String = "",
    val role: String = "",
    val phone: String = "",
    val email: String = "",
    val description: String = "",
)

fun Exposant.toFormData(): ExposantFormData {
    return ExposantFormData(
        name = name,
        role = actorType.firstOrNull().orEmpty(),
        phone = phone.orEmpty(),
        email = email.orEmpty(),
        description = description.orEmpty(),
    )
}

fun Exposant.typeLabel(): String {
    return actorType.firstOrNull()?.replace("_", " ") ?: "ACTOR"
}