package com.example.adspay.services

import android.content.Context
import com.example.adspay.models.auth.*
import com.example.adspay.models.common.BaseResponse

class AuthService(context: Context, private val baseUrl: String) {
    private val api = ApiClient.create(context, baseUrl).create(AuthApi::class.java)

    suspend fun login(username: String, password: String): BaseResponse<AuthData> {
        val request = AuthRequest(username, password)
        return api.login(request)
    }

    suspend fun refreshToken(refreshToken: String): BaseResponse<AuthData> {
        return api.refreshToken(refreshToken)
    }

    suspend fun logout(refreshToken: String): BaseResponse<AuthData> {
        return api.refreshToken(refreshToken)
    }
}
