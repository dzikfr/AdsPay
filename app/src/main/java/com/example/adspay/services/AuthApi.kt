package com.example.adspay.services

import com.example.adspay.models.auth.*
import retrofit2.http.*
import com.example.adspay.models.common.BaseResponse
import com.example.adspay.constant.ApiPath

interface AuthApi {
    @POST(ApiPath.Auth.LOGIN)
    suspend fun login(
        @Body loginRequest: AuthRequest
    ): BaseResponse<AuthData>

    @POST(ApiPath.Auth.REFRESH_TOKEN)
    suspend fun refreshToken(
        @Query("refreshToken") refreshToken: String
    ): BaseResponse<AuthData>

    @POST(ApiPath.Auth.LOGOUT)
    suspend fun logout(
        @Query("refreshToken") refreshToken: String
    ): BaseResponse<AuthData>
}
