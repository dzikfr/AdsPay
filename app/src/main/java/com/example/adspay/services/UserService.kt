package com.example.adspay.services

import retrofit2.http.GET
import retrofit2.http.Header
import com.example.adspay.models.user.*
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface UserService {
    @GET("api/mobile/users/profile")
    suspend fun getUserProfile(
        @Header("Authorization") bearer: String
    ): UserProfileResponse

    @GET("api/mobile/users/detail")
    suspend fun getUserDetail(
        @Header("Authorization") bearer: String
    ): UserDetailResponse

    @Multipart
    @POST("api/mobile/kyc/submit")
    suspend fun submitKyc(
        @Header("Authorization") token: String,
        @Part photo_selfie: MultipartBody.Part,
        @Part photo_ktp: MultipartBody.Part,
        @Part("metadata") metadata: RequestBody
    ): Any
}
