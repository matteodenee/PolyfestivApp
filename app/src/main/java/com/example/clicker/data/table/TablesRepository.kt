package com.example.clicker.data.table

class TablesRepository(private val api: TablesApiService) {
    suspend fun getTablesByFestival(festivalId: Int): List<TableDto> = api.getTablesByFestival(festivalId)
    suspend fun createTable(request: TableRequest): TableDto = api.createTable(request)
    suspend fun updateTable(id: Int, dto: TableDto): TableDto = api.updateTable(id, dto)
    suspend fun deleteTable(id: Int) = api.deleteTable(id)
}
