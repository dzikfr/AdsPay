package com.example.adspay.services

import android.content.Context
import com.example.adspay.models.auth.AuthResponse
import com.example.adspay.models.auth.AuthRequest

class AuthService(context: Context, private val baseUrl: String) {
    private val api = ApiClient.create(context, baseUrl).create(AuthApi::class.java)

    suspend fun login(username: String, password: String): AuthResponse {
        val request = AuthRequest(username, password)
        return api.login(request)
    }

    suspend fun refreshToken(refreshToken: String): AuthResponse {
        return api.refreshToken(refreshToken)
    }

    suspend fun logout(refreshToken: String): AuthResponse {
        return api.refreshToken(refreshToken)
    }
}
