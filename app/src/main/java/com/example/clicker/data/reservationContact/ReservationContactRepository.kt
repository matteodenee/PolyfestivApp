package com.example.clicker.data.reservationContact

class ReservationContactRepository(
    private val api: ReservationContactApiService
) {

    suspend fun getContactsByReservation(reservationId: Int): List<ReservationContactDto> {
        return api.getContacts(reservationId = reservationId)
    }

    suspend fun createContact(request: ReservationContactRequest): ReservationContactDto {
        return api.createContact(request)
    }
}