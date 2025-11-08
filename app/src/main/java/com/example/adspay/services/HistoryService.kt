package com.example.adspay.services

import retrofit2.http.*
import com.example.adspay.models.history.*
import com.example.adspay.models.common.BaseResponse
import com.example.adspay.constant.ApiPath

interface HistoryService {
    @GET(ApiPath.Transaction.GET_LIST)
    suspend fun getUserHistory(
        @Header("Authorization") bearer: String
    ): BaseResponse<HistoryData>

    @GET("${ApiPath.Transaction.GET_DETAIL}/{transactionCode}")
    suspend fun getTransactionDetail(
        @Header("Authorization") bearer: String,
        @retrofit2.http.Path("transactionCode") transactionCode: String
    ): BaseResponse<HistoryDetail>
}
