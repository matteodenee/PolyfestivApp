package com.example.clicker.data.reservationNote

class ReservationNoteRepository(
    private val api: ReservationNoteApiService
) {

    suspend fun getNotesByReservation(reservationId: Int): List<ReservationNoteDto> {
        return api.getNotes(reservationId = reservationId)
    }

    suspend fun createNote(request: ReservationNoteRequest): ReservationNoteDto {
        return api.createNote(request)
    }
}