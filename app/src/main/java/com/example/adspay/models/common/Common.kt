package com.example.adspay.models.common

data class BaseResponse<T>(
    val resp_code: String,
    val resp_message: String,
    val data: T? = null
)
