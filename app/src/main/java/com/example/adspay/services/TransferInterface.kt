package com.example.adspay.services

import retrofit2.http.Header
import retrofit2.http.Body
import com.example.adspay.models.transfer.*
import retrofit2.http.POST

interface TransferInterface {
    @POST("api/mobile/transfer/inquiry")
    suspend fun inquiryNumber(
        @Header("Authorization") bearer: String,
        @Body payload: InquiryPayload
    ): InquiryResponse

    @POST("/api/mobile/transfer/internal")
    suspend fun internalTransfer(
        @Header("Authorization") bearer: String,
        @Body payload: TransferPayload
    ): InternalTransferResponse
}