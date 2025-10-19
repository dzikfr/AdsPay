package com.example.adspay.models.transfer

data class InquiryPayload(
    val receiverPhone: String
)

data class InquiryResponse(
    val resp_code: String,
    val resp_message: String,
    val data: DataInquiry
)

data class DataInquiry(
    val exist: Boolean,
    val fullName: String,
    val phoneNumber: String
)

//Example respsoenya
//{
//    "resp_code": "00",
//    "resp_message": "Penerima ditemukan.",
//        "data": {
//        "exists": true,
//        "fullName": "Muhammad Azzam Pridana",
//        "phoneNumber": "082111867727"
//    }
//}

data class TransferPayload(
    val receiverPhone: String,
    val amount: Double,
    val note: String
)

data class TransferResponse(
    val resp_code: String,
    val resp_message: String,
    val data: DataTrasnfer
)

data class DataTrasnfer(
    val senderName: String,
    val receiverName: String,
    val amount: Double,
    val timestamp: String
)

// example responsnya
//{
//    "resp_code": "00",
//    "resp_message": "Sukses",
//    "data": {
//        "resp_code": "00",
//        "resp_message": "Transfer berhasil.",
//        "senderName": "Dzikri Fauzi",
//        "receiverName": "Nur Sarah",
//        "amount": 50000,
//        "timestamp": "2025-10-19T06:13:10.36650798"
//    }
//}