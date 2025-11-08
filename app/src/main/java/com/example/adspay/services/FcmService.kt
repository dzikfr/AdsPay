package com.example.adspay.services

import com.example.adspay.models.common.BaseResponse
import com.example.adspay.models.notification.*
import retrofit2.http.*
import com.example.adspay.constant.ApiPath

interface FcmInterface {

    @POST(ApiPath.Notification.SEND_FCM_TOKEN)
    suspend fun inquiryNumber(
        @Header("Authorization") bearer: String,
        @Body payload: FcmTokenRequest
    ): BaseResponse<FcmTokenDataResponse>

}
