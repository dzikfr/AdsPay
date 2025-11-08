package com.example.adspay.services

import com.example.adspay.models.common.BaseResponse
import com.example.adspay.models.qris.*
import retrofit2.http.*
import com.example.adspay.constant.ApiPath

interface QrisService {

    @POST(ApiPath.Qris.VALIDATE_QRIS)
    suspend fun validateQris(
        @Header("Authorization") bearer: String,
        @Body payload: ValidateQrisPayload
    ): BaseResponse<ValidateQrisDataResponse>

    @POST(ApiPath.Qris.PAY)
    suspend fun payDynamic(
        @Header("Authorization") bearer: String,
        @Body payload: PayDynamicPayload
    ): BaseResponse<PayDataResponse>

    @POST(ApiPath.Qris.PAY)
    suspend fun payStatic(
        @Header("Authorization") bearer: String,
        @Body payload: PayStaticPayload
    ): BaseResponse<PayDataResponse>
}
