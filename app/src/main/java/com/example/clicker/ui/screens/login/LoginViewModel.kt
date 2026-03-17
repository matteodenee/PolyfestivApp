package com.example.clicker.ui.screens.login

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.auth.AuthRepository
import com.example.clicker.data.datastore.SessionPreferencesRepository
import com.example.clicker.data.network.SessionCookieHolder
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionPreferencesRepository: SessionPreferencesRepository
) : ViewModel() {

    private val loginTextState = mutableStateOf("")
    val loginText: State<String> = loginTextState

    private val passwordTextState = mutableStateOf("")
    val passwordText: State<String> = passwordTextState

    private val internalState = mutableStateOf<LoginUiState>(LoginUiState.Idle)
    val state: State<LoginUiState> = internalState

    fun onLoginChange(value: String) {
        loginTextState.value = value
    }

    fun onPasswordChange(value: String) {
        passwordTextState.value = value
    }

    fun login(onSuccessNavigate: () -> Unit) {
        val login = loginTextState.value.trim()
        val password = passwordTextState.value

        if (login.isBlank() || password.isBlank()) {
            internalState.value = LoginUiState.Error("Remplis le login et le mot de passe")
            Log.d(TAG, "Login refusé : champs vides")
            return
        }

        viewModelScope.launch {
            internalState.value = LoginUiState.Loading
            try {
                val result = authRepository.login(login, password)

                val accessCookie = result.accessCookie
                val refreshCookie = result.refreshCookie

                if (accessCookie.isNullOrBlank() || refreshCookie.isNullOrBlank()) {
                    throw Exception("Cookies de session absents dans la réponse")
                }

                SessionCookieHolder.accessCookie = accessCookie
                SessionCookieHolder.refreshCookie = refreshCookie

                sessionPreferencesRepository.saveSession(accessCookie, refreshCookie)

                Log.d(TAG, "Connexion réussie pour : ${result.response.user.login}")
                internalState.value = LoginUiState.Success(result.response.user)

                onSuccessNavigate()
            } catch (e: Exception) {
                Log.e(TAG, "Erreur de connexion", e)
                internalState.value =
                    LoginUiState.Error("Échec de connexion " + (e.message ?: ""))
            }
        }
    }

    fun restoreSessionIfNeeded(onSessionFound: () -> Unit) {
        viewModelScope.launch {
            val savedAccessCookie = sessionPreferencesRepository.accessCookie.first()
            val savedRefreshCookie = sessionPreferencesRepository.refreshCookie.first()

            if (savedAccessCookie.isNullOrBlank() || savedRefreshCookie.isNullOrBlank()) {
                Log.d(TAG, "Aucune session sauvegardée")
                return@launch
            }

            Log.d(TAG, "Cookies trouvés dans DataStore")

            SessionCookieHolder.accessCookie = savedAccessCookie
            SessionCookieHolder.refreshCookie = savedRefreshCookie

            val sessionValid = authRepository.checkSession()

            if (sessionValid) {
                Log.d(TAG, "Session valide avec access token actuel")
                onSessionFound()
            } else {
                Log.d(TAG, "Access token expiré, tentative de refresh")

                val newAccessCookie = authRepository.refreshSession()

                if (!newAccessCookie.isNullOrBlank()) {
                    SessionCookieHolder.accessCookie = newAccessCookie
                    sessionPreferencesRepository.updateAccessCookie(newAccessCookie)

                    Log.d(TAG, "Session restaurée après refresh")
                    onSessionFound()
                } else {
                    Log.d(TAG, "Refresh expiré ou invalide, suppression de la session")

                    SessionCookieHolder.accessCookie = null
                    SessionCookieHolder.refreshCookie = null
                    sessionPreferencesRepository.clearSession()
                }
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            SessionCookieHolder.accessCookie = null
            SessionCookieHolder.refreshCookie = null
            sessionPreferencesRepository.clearSession()
            internalState.value = LoginUiState.Idle
            onLoggedOut()
        }
    }
}