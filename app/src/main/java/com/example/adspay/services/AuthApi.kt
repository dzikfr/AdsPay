package com.example.adspay.services

import com.example.adspay.models.auth.AuthResponse
import retrofit2.http.*

interface AuthApi {
    @FormUrlEncoded
    @POST("realms/AdsPay-Client/protocol/openid-connect/token")
    suspend fun login(
        @Field("grant_type") grantType: String = "password",
        @Field("client_id") clientId: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): AuthResponse

    @FormUrlEncoded
    @POST("realms/AdsPay-Client/protocol/openid-connect/token")
    suspend fun refreshToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("client_id") clientId: String,
        @Field("refresh_token") refreshToken: String
    ): AuthResponse
}
