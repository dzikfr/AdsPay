package com.example.adspay.services

import android.content.Context
import com.example.adspay.models.auth.OtpResponse
import com.example.adspay.models.auth.OtpValidateResponse
import com.example.adspay.models.auth.RegisterRequest
import com.example.adspay.models.auth.RegisterResponse

class RegisterService(context: Context) {
    private val baseUrl = "http://38.47.94.165:3123"
    private val api = ApiClient.create(context, baseUrl).create(RegisterApi::class.java)

    suspend fun sendOtp(phone: String): OtpResponse {
        return api.sendOtp(phone)
    }

    suspend fun validateOtp(phone: String, code: String): OtpValidateResponse {
        return api.validateOtp(phone, code)
    }

    suspend fun registerUser(request: RegisterRequest, registrationToken: String): RegisterResponse {
        return api.register(request, registrationToken)
    }
}
