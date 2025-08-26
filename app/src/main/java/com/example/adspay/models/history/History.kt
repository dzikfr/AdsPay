package com.example.adspay.models.history

data class History(
    val id: String,
    val type: String,
    val iconUrl: String,
    val actionName: String,
    val dateTime: String,
    val amount: Double,
    val isIncome: Boolean,
    val isSuccess: Boolean
)