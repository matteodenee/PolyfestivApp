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
import android.content.Context
import com.example.clicker.ui.utils.network.NetworkUtils

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val sessionPreferencesRepository: SessionPreferencesRepository,
    private val context: Context // pour savoir si internet est disponible
) : ViewModel() {

    private val loginTextState = mutableStateOf("")
    val loginText: State<String> = loginTextState

    private val passwordTextState = mutableStateOf("")
    val passwordText: State<String> = passwordTextState

    private val internalState = mutableStateOf<LoginUiState>(LoginUiState.Idle)
    val state: State<LoginUiState> = internalState


    private val currentUserRoleState = mutableStateOf<String?>(null)
    val currentUserRole: State<String?> = currentUserRoleState

    fun isAdmin(): Boolean {
        return currentUserRoleState.value.equals("admin", ignoreCase = true)
    }

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

                val userRole = result.response.user.role
                currentUserRoleState.value = userRole

                sessionPreferencesRepository.saveSession(accessCookie, refreshCookie, userRole)

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
            val savedUserRole = sessionPreferencesRepository.userRole.first()

            if (savedAccessCookie.isNullOrBlank() || savedRefreshCookie.isNullOrBlank()) {
                Log.d(TAG, "Aucune session sauvegardée")
                return@launch
            }

            Log.d(TAG, "Cookies trouvés dans DataStore")

            SessionCookieHolder.accessCookie = savedAccessCookie
            SessionCookieHolder.refreshCookie = savedRefreshCookie
            currentUserRoleState.value = savedUserRole

            // Si l’utilisateur a déjà une session sauvegardée et qu’il n’a pas Internet, on ne bloque pas l’accès, on le laisse entrer en offline
            if (!NetworkUtils.isInternetAvailable(context)) {
                Log.d(TAG, "Mode hors ligne : accès autorisé avec session locale")
                onSessionFound()
                return@launch
            }

            try {
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
            } catch (e: Exception) {
                Log.e(TAG, "Impossible de restaurer la session", e)
            }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        viewModelScope.launch {
            SessionCookieHolder.accessCookie = null
            SessionCookieHolder.refreshCookie = null
            currentUserRoleState.value = null
            sessionPreferencesRepository.clearSession()
            internalState.value = LoginUiState.Idle
            onLoggedOut()
        }
    }
}