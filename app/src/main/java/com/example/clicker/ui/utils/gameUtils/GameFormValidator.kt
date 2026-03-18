package com.example.clicker.ui.utils.gameUtils

object GameFormValidator {
    fun validate(form: GameFormState): String? {
        if (form.name.isBlank()) return "Le nom est obligatoire"
        if (form.author.isBlank()) return "L'auteur est obligatoire"
        if (form.type.isBlank()) return "Le type est obligatoire"

        val editorId = form.editorId.toIntOrNull()
        if (editorId == null || editorId <= 0) {
            return "Editor ID invalide"
        }

        val nbMin = form.nbMinPlayers.toIntOrNull()
        val nbMax = form.nbMaxPlayers.toIntOrNull()

        if (nbMin == null || nbMin <= 0) {
            return "Nombre minimum de joueurs invalide"
        }

        if (nbMax == null || nbMax <= 0) {
            return "Nombre maximum de joueurs invalide"
        }

        if (nbMin > nbMax) {
            return "Le nombre minimum de joueurs ne peut pas dépasser le maximum"
        }

        return null
    }
}