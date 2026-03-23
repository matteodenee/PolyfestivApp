package com.example.clicker.ui.screens.admin

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clicker.TAG
import com.example.clicker.data.admin.AdminRepository
import com.example.clicker.data.admin.AdminUserDto
import kotlinx.coroutines.launch

class AdminViewModel(
    private val adminRepository: AdminRepository
) : ViewModel() {

    private val internalState = mutableStateOf<AdminUiState>(AdminUiState.Loading)
    val state: State<AdminUiState> = internalState

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            internalState.value = AdminUiState.Loading
            try {
                val users = adminRepository.getUsers()
                Log.d(TAG, "Utilisateurs admin chargés : ${users.size}")
                internalState.value = AdminUiState.Success(users)
            } catch (e: Exception) {
                Log.e(TAG, "Erreur chargement utilisateurs admin", e)
                internalState.value =
                    AdminUiState.Error("Impossible de charger les utilisateurs")
            }
        }
    }

    fun validateUser(id: Int) {
        viewModelScope.launch {
            try {
                val updatedUser = adminRepository.validateUser(id)
                Log.d(TAG, "Utilisateur validé : $id")
                replaceUserInState(updatedUser)
            } catch (e: Exception) {
                Log.e(TAG, "Erreur validation utilisateur", e)
                internalState.value =
                    AdminUiState.Error("Impossible de valider l'utilisateur")
            }
        }
    }

    fun updateUserRole(id: Int, role: String) {
        viewModelScope.launch {
            try {
                val updatedUser = adminRepository.updateUserRole(id, role)
                Log.d(TAG, "Rôle modifié pour utilisateur $id -> $role")
                replaceUserInState(updatedUser)
            } catch (e: Exception) {
                Log.e(TAG, "Erreur modification rôle", e)
                internalState.value =
                    AdminUiState.Error("Impossible de modifier le rôle")
            }
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            try {
                adminRepository.deleteUser(id)
                Log.d(TAG, "Utilisateur supprimé définitivement : $id")
                removeUserFromState(id)
            } catch (e: Exception) {
                Log.e(TAG, "Erreur suppression utilisateur $id", e)
                internalState.value =
                    AdminUiState.Error("Impossible de supprimer l'utilisateur")
            }
        }
    }

    private fun replaceUserInState(updatedUser: AdminUserDto) {
        val currentState = internalState.value
        if (currentState is AdminUiState.Success) {
            internalState.value = AdminUiState.Success(
                currentState.users.map { user ->
                    if (user.id == updatedUser.id) updatedUser else user
                }
            )
        } else {
            loadUsers()
        }
    }

    private fun removeUserFromState(id: Int) {
        val currentState = internalState.value
        if (currentState is AdminUiState.Success) {
            internalState.value = AdminUiState.Success(
                currentState.users.filterNot { user -> user.id == id }
            )
        } else {
            loadUsers()
        }
    }
}