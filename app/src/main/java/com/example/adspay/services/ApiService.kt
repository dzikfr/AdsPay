package com.example.adspay.services

import com.example.adspay.models.user.LoginRequest
import com.example.adspay.models.user.LoginResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body request: LoginRequest): LoginResponse
}
