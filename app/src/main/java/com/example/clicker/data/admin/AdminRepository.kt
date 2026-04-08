package com.example.clicker.data.admin

class AdminRepository(
    private val api: AdminApiService
) {

    suspend fun getUsers(): List<AdminUserDto> {
        return api.getUsers()
    }

    suspend fun validateUser(id: Int): AdminUserDto {
        return api.validateUser(id)
    }

    suspend fun updateUserRole(id: Int, role: String): AdminUserDto {
        return api.updateUserRole(
            id = id,
            request = UserRoleRequest(role = role)
        )
    }

    suspend fun deleteUser(id: Int) {
        api.deleteUser(id)
    }
}