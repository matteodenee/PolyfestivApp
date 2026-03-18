package com.example.clicker.ui.utils.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object NetworkUtils {

    fun isInternetAvailable(context: Context): Boolean {
        // Récupère le service réseau du système
        val service = context.getSystemService(Context.CONNECTIVITY_SERVICE)

        if (service == null) {
            return false
        }

        val connectivityManager = service as ConnectivityManager

        // Récupère le réseau actuellement actif
        val network = connectivityManager.activeNetwork

        if (network == null) {
            return false
        }

        // Récupère les capacités du réseau
        val capabilities = connectivityManager.getNetworkCapabilities(network)

        // Si aucune info disponible, on considère qu'il n'y a pas Internet
        if (capabilities == null) {
            return false
        }

        // Vérifie si le réseau supporte Internet
        val hasInternetCapability =
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

        // Vérifie si la connexion est réellement fonctionnelle
        val isValidated =
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        // Retourne true uniquement si Internet est disponible et valide
        return hasInternetCapability && isValidated
    }
}