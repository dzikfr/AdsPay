package com.example.adspay.services

import com.example.adspay.models.auth.*
import retrofit2.http.*
import com.example.adspay.models.common.BaseResponse
import com.example.adspay.constant.ApiPath

interface RegisterApi {
    @POST(ApiPath.Auth.REQUEST_OTP)
    suspend fun sendOtp(
        @Query("phoneNumber") phoneNumber: String
    ): BaseResponse<OtpData>

    @POST(ApiPath.Auth.VALIDATE_OTP)
    suspend fun validateOtp(
        @Query("phoneNumber") phoneNumber: String,
        @Query("code") code: String
    ): BaseResponse<OtpValidateData>

    @POST(ApiPath.Auth.REGISTER)
    suspend fun register(
        @Body request: RegisterRequest,
        @Header("X-Registration-Token") registrationToken: String
    ): BaseResponse<UserData>
}
