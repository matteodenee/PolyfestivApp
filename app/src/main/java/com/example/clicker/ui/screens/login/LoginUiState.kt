package com.example.clicker.ui.screens.login

import com.example.clicker.data.local.UserEntity


data class LoginUiState(
    val currentUser: UserEntity? = null,
    val isLoggedIn: Boolean = false
)