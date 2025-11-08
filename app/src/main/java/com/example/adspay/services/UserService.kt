package com.example.adspay.services

import retrofit2.http.*
import com.example.adspay.models.user.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import com.example.adspay.models.common.BaseResponse
import com.example.adspay.constant.ApiPath

interface UserService {
    @GET(ApiPath.Account.GET_PROFILE)
    suspend fun getUserProfile(
        @Header("Authorization") bearer: String
    ): BaseResponse<UserData>

    @GET(ApiPath.Account.GET_DETAIL_USER)
    suspend fun getUserDetail(
        @Header("Authorization") bearer: String
    ): BaseResponse<UserDetailData>

    @Multipart
    @POST(ApiPath.Kyc.SUBMIT_KYC)
    suspend fun submitKyc(
        @Header("Authorization") token: String,
        @Part photo_selfie: MultipartBody.Part,
        @Part photo_ktp: MultipartBody.Part,
        @Part("metadata") metadata: RequestBody
    ): Any
}
