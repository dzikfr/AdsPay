package com.example.adspay.services

import android.content.Context
import com.example.adspay.models.auth.AuthResponse

class AuthService(context: Context, private val baseUrl: String) {
    private val api = ApiClient.create(context, baseUrl).create(AuthApi::class.java)

    suspend fun login(clientId: String, username: String, password: String): AuthResponse {
        return api.login(clientId = clientId, username = username, password = password)
    }

    suspend fun refreshToken(clientId: String, refreshToken: String): AuthResponse {
        return api.refreshToken(clientId = clientId, refreshToken = refreshToken)
    }
}
