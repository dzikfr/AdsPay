package com.example.adspay.services

import com.example.adspay.models.auth.OtpResponse
import com.example.adspay.models.auth.OtpValidateResponse
import com.example.adspay.models.auth.RegisterRequest
import com.example.adspay.models.auth.RegisterResponse
import retrofit2.http.*

interface RegisterApi {
    @POST("api/mobile/otp/send")
    suspend fun sendOtp(
        @Query("phoneNumber") phoneNumber: String
    ): OtpResponse

    @POST("api/mobile/otp/validate")
    suspend fun validateOtp(
        @Query("phoneNumber") phoneNumber: String,
        @Query("code") code: String
    ): OtpValidateResponse

    @POST("api/mobile/users/register")
    suspend fun register(
        @Body request: RegisterRequest,
        @Header("X-Registration-Token") registrationToken: String
    ): RegisterResponse
}
