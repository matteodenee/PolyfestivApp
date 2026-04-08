package com.example.clicker.data.invoice

class InvoiceRepository(
    private val api: InvoiceApiService
) {

    suspend fun getInvoicesByReservation(reservationId: Int): List<InvoiceDto> {
        return api.getInvoices(reservationId = reservationId)
    }

    suspend fun createInvoice(request: InvoiceRequest): InvoiceDto {
        return api.createInvoice(request)
    }

    suspend fun markInvoicePaid(id: Int): InvoiceDto {
        return api.markInvoicePaid(id)
    }
}