package com.example.adspay.services

import retrofit2.http.GET
import retrofit2.http.Header
import com.example.adspay.models.history.*
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface HistoryService {
    @GET("api/mobile/transactions/history")
    suspend fun getUserHistory(
        @Header("Authorization") bearer: String
    ): HistoryResponse

    @GET("api/mobile/transactions/{transactionCode}")
    suspend fun getTransactionDetail(
        @Header("Authorization") bearer: String,
        @retrofit2.http.Path("transactionCode") transactionCode: String
    ): HistoryDetailResponse
}
