package com.example.adspay.models.user

data class LoginResponse(
    val token: String,
    val userId: String,
    val username: String
)