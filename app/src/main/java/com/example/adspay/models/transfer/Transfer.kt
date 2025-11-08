package com.example.adspay.models.transfer

data class InquiryPayload(
    val receiverPhone: String
)

data class DataInquiry(
    val exists: Boolean,
    val fullName: String?,
    val phoneNumber: String
)

data class TransferPayload(
    val receiverPhone: String,
    val amount: Double,
    val note: String
)

data class DataTransfer(
    val senderName: String,
    val receiverName: String,
    val amount: Double,
    val timestamp: String
)

data class InquiryBankPayload(
    val bankCode: String,
    val accountNumber: String
)

data class GetBankDataResponse(
    val bankCode: String,
    val bankName: String
)

data class DataInquiryBank(
    val accountName: String,
    val accountNumber: String,
    val bankName: String
)

data class TransferBankPreviewPayload(
    val bankCode: String,
    val accountNumber: String,
    val amount: Double,
    val description: String?
)

data class DataPreviewBankTransfer(
    val bankCode: String,
    val bankName: String,
    val accountNumber: String,
    val accountName: String,
    val amount: Double,
    val userFee: Double,
    val bankFee: Double,
    val totalDebit: Double
)

data class TransferBankPayload(
    val bankCode: String,
    val accountNumber: String,
    val amount: Double,
    val description: String?,
    val pin: String
)

data class DataBankTransfer(
    val transactionRef: String,
    val bankName: String,
    val accountNumber: String,
    val amount: Double,
    val status: String,
    val timestamp: String,
    val userFee: Double,
    val bankFee: Double,
    val totalDebit: Double
)
