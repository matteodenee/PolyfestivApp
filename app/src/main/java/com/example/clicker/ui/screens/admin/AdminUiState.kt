package com.example.clicker.ui.screens.admin

import com.example.clicker.data.admin.AdminUserDto

sealed interface AdminUiState {
    data object Loading : AdminUiState
    data class Success(val users: List<AdminUserDto>) : AdminUiState
    data class Error(val message: String) : AdminUiState
}