package com.example.adspay.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.adspay.screens.*
import com.example.adspay.screens.home.*
import com.example.adspay.screens.history.*
import com.example.adspay.screens.promotion.*
import com.example.adspay.screens.transfer.*
import com.example.adspay.services.RegisterService
import androidx.compose.ui.platform.LocalContext
import com.example.adspay.screens.register.RegisterFormScreen
import com.example.adspay.screens.register.RegisterOtpScreen
import com.example.adspay.screens.register.RegisterPhoneScreen
import com.example.adspay.screens.register.RegisterPinScreen
import com.example.adspay.screens.register.KycFormScreen
import com.example.adspay.screens.topup.*

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isDarkMode: Boolean,
    toggleTheme: () -> Unit,
    startDestination: String
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("login") {LoginScreen((navController))}
        composable("home") { HomeScreen(navController) }
        composable("qris") { QrisScreen() }
        composable("profile") { ProfileScreen(isDarkMode, toggleTheme, navController) }
        composable("pulsa") { PulsaScreen(navController) }
        composable("kycStart") { KycInitScreen(navController) }
        composable("kyc") { KycFormScreen(navController) }
        composable("token") { TokenScreen(navController) }
        composable("topup_guide") { TopupGuide(navController) }
        composable("internal_transfer") { InternalTransferScreen(navController) }
        composable("bank_transfer") { BankListScreen(navController) }
        composable("bankInquiry/{bankCode}/{bankName}") {
            val args = it.arguments!!
            BankInquiryScreen(navController, args.getString("bankCode")!!, args.getString("bankName")!!)
        }
        composable("bankAmount/{bankCode}/{bankName}/{accountNumber}/{accountName}") {
            val args = it.arguments!!
            BankAmountScreen(
                navController,
                args.getString("bankCode")!!,
                args.getString("bankName")!!,
                args.getString("accountNumber")!!,
                args.getString("accountName")!!
            )
        }
        composable("bankConfirm/{bankCode}/{accountNumber}/{amount}/{remark}") {
            val args = it.arguments!!
            BankConfirmScreen(
                navController,
                args.getString("bankCode")!!,
                args.getString("accountNumber")!!,
                args.getString("amount")!!,
                args.getString("remark")!!
            )
        }
        composable("transferSummary/{receiverName}/{amount}/{note}") { backStackEntry ->
            val receiverName = backStackEntry.arguments?.getString("receiverName") ?: ""
            val amount = backStackEntry.arguments?.getString("amount") ?: ""
            val note = backStackEntry.arguments?.getString("note") ?: ""
            TransferSummaryScreen(navController, receiverName, amount, note)
        }
        composable("promotion") { PromotionScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("historyDetail/{historyId}") { backStackEntry ->
            val historyId = backStackEntry.arguments?.getString("historyId") ?: ""
            HistoryDetailScreen(navController, historyId)
        }
        composable("promotionDetail/{promoId}") { backStackEntry ->
            val promoId = backStackEntry.arguments?.getString("promoId") ?: ""
            PromotionDetailScreen(navController, promoId)
        }
        composable("registerPhone") {
            RegisterPhoneScreen(navController, RegisterService(LocalContext.current))
        }
        composable("registerOtp/{phone}") { backStack ->
            val phone = backStack.arguments?.getString("phone") ?: ""
            RegisterOtpScreen(navController, RegisterService(LocalContext.current), phone)
        }
        composable("registerForm/{phone}") { backStack ->
            val phone = backStack.arguments?.getString("phone") ?: ""
            RegisterFormScreen(navController = navController, initialPhone = phone)
        }
        composable("registerPin") {
            val registerService = RegisterService(LocalContext.current)
            RegisterPinScreen(navController = navController, registerService = registerService)
        }
    }
}
