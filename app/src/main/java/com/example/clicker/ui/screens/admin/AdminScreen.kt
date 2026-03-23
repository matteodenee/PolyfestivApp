package com.example.clicker.ui.screens.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.clicker.data.admin.AdminRoles
import com.example.clicker.data.admin.AdminUserDto
import com.example.clicker.ui.theme.ButtonBlue
import com.example.clicker.ui.theme.ButtonGreen
import com.example.clicker.ui.theme.ButtonOrange
import com.example.clicker.ui.viewmodel.AppViewModelProvider

@Composable
fun AdminScreen(
    refreshKey: Int,
    modifier: Modifier = Modifier,
    viewModel: AdminViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )
) {
    val state = viewModel.state.value
    var userToEdit by remember { mutableStateOf<AdminUserDto?>(null) }

    LaunchedEffect(refreshKey) {
        viewModel.loadUsers()
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when (val uiState = state) {
            is AdminUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            is AdminUiState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            is AdminUiState.Success -> {
                val pendingUsers = uiState.users.filter { !it.validated }
                val validatedUsers = uiState.users.filter { it.validated }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 22.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(28.dp))

                            Text(
                                text = "Nouveaux utilisateurs",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )

                            Spacer(modifier = Modifier.height(18.dp))
                        }

                        if (pendingUsers.isEmpty()) {
                            item {
                                EmptySectionText("Aucun nouvel utilisateur")
                                Spacer(modifier = Modifier.height(28.dp))
                            }
                        } else {
                            items(
                                items = pendingUsers,
                                key = { user -> user.id }
                            ) { user ->
                                PendingUserItem(
                                    user = user,
                                    onAccept = { viewModel.validateUser(user.id) },
                                    onRefuse = { viewModel.deleteUser(user.id) }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(26.dp))

                            Text(
                                text = "Gestion des comptes",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            )

                            Spacer(modifier = Modifier.height(18.dp))
                        }

                        if (validatedUsers.isEmpty()) {
                            item {
                                EmptySectionText("Aucun compte validé")
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        } else {
                            items(
                                items = validatedUsers,
                                key = { user -> user.id }
                            ) { user ->
                                ManagedUserItem(
                                    user = user,
                                    onModify = {
                                        userToEdit = user
                                    }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }

                userToEdit?.let { selectedUser ->
                    RolePickerDialog(
                        currentUser = selectedUser,
                        onDismiss = { userToEdit = null },
                        onConfirm = { selectedRole ->
                            viewModel.updateUserRole(
                                id = selectedUser.id,
                                role = selectedRole
                            )
                            userToEdit = null
                        },
                        onDeleteUser = {
                            viewModel.deleteUser(selectedUser.id)
                            userToEdit = null
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun PendingUserItem(
    user: AdminUserDto,
    onAccept: () -> Unit,
    onRefuse: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.login,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = user.role.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onRefuse,
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonOrange,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("Refuser")
                }

                Button(
                    onClick = onAccept,
                    shape = RoundedCornerShape(22.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonOrange,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("Accepter")
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)
        )
    }
}

@Composable
private fun ManagedUserItem(
    user: AdminUserDto,
    onModify: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 22.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = user.login,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = user.role.uppercase(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onModify,
                shape = RoundedCornerShape(22.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonBlue,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("Modifier")
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.18f)
        )
    }
}

@Composable
private fun EmptySectionText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        modifier = modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.65f)
    )
}

@Composable
private fun RolePickerDialog(
    currentUser: AdminUserDto,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    onDeleteUser: () -> Unit
) {
    var selectedRole by remember(currentUser) {
        mutableStateOf(currentUser.role)
    }
    var showDeleteConfirmation by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Modifier le rôle",
                    style = MaterialTheme.typography.titleLarge
                )

                IconButton(
                    onClick = {
                        showDeleteConfirmation = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Supprimer l'utilisateur",
                        tint = ButtonOrange
                    )
                }
            }
        },
        text = {
            Column {
                Text(
                    text = "Utilisateur : ${currentUser.login}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(16.dp))

                AdminRoles.allRoles.forEach { role ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedRole == role,
                            onClick = {
                                selectedRole = role
                            },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = ButtonBlue,
                                unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            )
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = role.uppercase(),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(selectedRole)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonGreen,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("Valider")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ButtonOrange,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("Annuler")
            }
        }
    )

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            containerColor = MaterialTheme.colorScheme.background,
            title = {
                Text("Confirmer la suppression")
            },
            text = {
                Text("Voulez-vous vraiment supprimer définitivement ${currentUser.login} ?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmation = false
                        onDeleteUser()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonOrange,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("Supprimer")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDeleteConfirmation = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ButtonBlue,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text("Annuler")
                }
            }
        )
    }
}