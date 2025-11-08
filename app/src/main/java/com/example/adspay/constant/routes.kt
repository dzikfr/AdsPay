package com.example.adspay.constant

object ApiConfig {
    const val BASE_URL = "http://38.47.94.165:3123/"
}

object ApiPath {
    object Auth {
        const val REQUEST_OTP = "api/mobile/otp/send"
        const val VALIDATE_OTP = "api/mobile/otp/validate"
        const val REGISTER = "api/mobile/users/register"
        const val LOGIN = "api/mobile/auth/login"
        const val LOGOUT = "api/mobile/auth/logout"
        const val REFRESH_TOKEN = "api/mobile/auth/refresh"
    }

    object Account {
        const val GET_PROFILE = "api/mobile/users/profile"
        const val GET_DETAIL_USER = "api/mobile/users/detail"
    }

    object Kyc {
        const val SUBMIT_KYC = "api/mobile/kyc/submit"
    }

    object Transaction {
        const val GET_LIST = "api/mobile/transactions/history"
        const val GET_DETAIL = "api/mobile/transactions"
    }

    object Transfer {
        object Internal {
            const val INQUIRY = "api/mobile/transfer/inquiry"
            const val TRANSFER = "api/mobile/transfer/internal"
        }

        object Bank {
            const val GET_LIST_BANK = "api/mobile/bank/list"
            const val INQUIRY = "api/mobile/bank/inquiry"
            const val PREVIEW = "api/mobile/bank/preview"
            const val TRANSFER = "/api/mobile/bank/transfer"
        }
    }

    object Qris {
        const val VALIDATE_QRIS = "api/mobile/qris/validate"
        const val PAY = "api/mobile/qris/pay"
    }
}