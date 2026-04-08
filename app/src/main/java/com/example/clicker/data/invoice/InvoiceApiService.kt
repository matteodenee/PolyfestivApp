package com.example.clicker.data.invoice

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface InvoiceApiService {

    @GET("api/invoices")
    suspend fun getInvoices(
        @Query("reservationId") reservationId: Int? = null,
        @Query("festivalId") festivalId: Int? = null
    ): List<InvoiceDto>

    @POST("api/invoices")
    suspend fun createInvoice(
        @Body request: InvoiceRequest
    ): InvoiceDto

    @POST("api/invoices/{id}/pay")
    suspend fun markInvoicePaid(
        @Path("id") id: Int
    ): InvoiceDto
}