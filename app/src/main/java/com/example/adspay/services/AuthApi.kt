package com.example.adspay.services

import com.example.adspay.models.auth.AuthResponse
import retrofit2.http.*
import com.example.adspay.models.auth.AuthRequest

interface AuthApi {
    @POST("api/mobile/auth/login")
    suspend fun login(
        @Body loginRequest: AuthRequest
    ): AuthResponse

    @POST("api/mobile/auth/refresh")
    suspend fun refreshToken(
        @Query("refreshToken") refreshToken: String
    ): AuthResponse

    @POST("api/mobile/auth/logout")
    suspend fun logout(
        @Query("refreshToken") refreshToken: String
    ): AuthResponse
}
