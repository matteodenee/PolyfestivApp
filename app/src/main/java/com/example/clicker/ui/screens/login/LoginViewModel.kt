package com.example.clicker.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.clicker.data.local.UserEntity
import com.example.clicker.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val uiState: StateFlow<LoginUiState> =
        authRepository.getCurrentUserStream()
            .map { user ->
                LoginUiState(
                    currentUser = user,
                    isLoggedIn = user != null
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = LoginUiState()
            )

    fun insertTestUser() {
        viewModelScope.launch {
            authRepository.insertUser(
                UserEntity(
                    id = 1,
                    login = "test",
                    role = "user",
                    validated = true
                )
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.deleteAllUsers()
        }
    }
}