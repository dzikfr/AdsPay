package com.example.adspay.models.user

data class UserData(
    val userId: String,
    val phoneNumber: String,
    val fullName: String,
    val saldo: Int,
    val status: String,
    val registrationStatus: String,
    val vaList: List<VaList>
)

data class VaList(
    val id: Int,
    val vaNumber: String,
    val vaName: String,
    val vaType: String,
    val vaStatus: String,
    val vaBalance: Double,
    val vaCreatedAt: String,
    val vaUpdatedAt: String
)

data class UserDetailData(
    val id: Int,
    val phoneNumber: String,
    val status: String,
    val registrationStatus: String,
    val saldo: Double,
    val fullName : String?,
    val nik : String?,
    val placeOfBirth : String?,
    val dob : String?,
    val gender : String?,
    val martialStatus : String?,
    val job : String?,
    val selfieUrl : String?,
    val ktpUrl : String?,
    val kycStatus : String?,
    val rejectedReason : String?,
    val requestId : String?,
    val verifiedAt : String?
)