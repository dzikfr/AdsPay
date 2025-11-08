package com.example.adspay.services

import android.content.Context
import com.example.adspay.models.auth.*
import com.example.adspay.models.common.BaseResponse

class RegisterService(context: Context) {
    private val baseUrl = "http://38.47.94.165:3123"
    private val api = ApiClient.create(context, baseUrl).create(RegisterApi::class.java)

    suspend fun sendOtp(phone: String): BaseResponse<OtpData> {
        return api.sendOtp(phone)
    }

    suspend fun validateOtp(phone: String, code: String): BaseResponse<OtpValidateData> {
        return api.validateOtp(phone, code)
    }

    suspend fun registerUser(request: RegisterRequest, registrationToken: String): BaseResponse<UserData> {
        return api.register(request, registrationToken)
    }
}
