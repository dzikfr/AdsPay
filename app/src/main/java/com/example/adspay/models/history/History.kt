package com.example.adspay.models.history

data class HistoryResponse(
    val resp_code: String,
    val resp_message: String,
    val data: HistoryData
)

data class HistoryData(
    val content: List<HistoryItem>,
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val pageSize: Int
)

data class HistoryItem(
    val transactionCode: String,
    val type: String,
    val direction: String,
    val amount: Double,
    val balanceAfter: Double,
    val channel: String,
    val status: String,
    val description: String,
    val createdAt: String
)

data class HistoryDetailResponse(
    val resp_code: String,
    val resp_message: String,
    val data: HistoryDetail
)

data class HistoryDetail(
    val transactionCode: String,
    val type: String,
    val direction: String,
    val amount: Double,
    val balanceAfter: Double,
    val referenceId: String,
    val channel: String,
    val externalSource: String,
    val status: String,
    val description: String,
    val createdAt: String,
    val updatedAt: String
)
