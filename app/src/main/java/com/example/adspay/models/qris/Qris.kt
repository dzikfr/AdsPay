package com.example.adspay.models.qris


data class ValidateQrisPayload(
    val qrString: String
)

data class ValidateQrisDataResponse(
    val nmid: String,
    val merchantName: String,
    val merchantCity: String,
    val amount: Double? = null, // null if STATIC, available if DYNAMIC
    val type: String,
    val transactionRef: String
)

data class PayDynamicPayload(
    val nmid: String,
    val merchantName: String,
    val merchantCity: String,
    val amount: Double,
    val transactionRef: String,
    val type: String,
    val pin: String
)


data class PayStaticPayload(
    val nmid: String,
    val merchantName: String,
    val merchantCity: String,
    val amount: Double,
    val type: String,
    val pin: String
)

data class PayDataResponse(
    val transactionRef: String? = null,
    val merchantName: String,
    val amount: Double,
    val userFee: Double,
    val merchantFee: Double,
    val status: String,
    val timestamp: String
)