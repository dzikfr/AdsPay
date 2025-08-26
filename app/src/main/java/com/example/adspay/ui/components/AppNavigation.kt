package com.example.adspay.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.adspay.screens.*
import com.example.adspay.screens.home.*
import com.example.adspay.screens.history.*

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
        composable("token") { TokenScreen(navController) }
        composable("promotion") { PromotionScreen(navController) }
        composable("history") { HistoryScreen(navController) }
        composable("historyDetail/{historyId}") { backStackEntry ->
            val historyId = backStackEntry.arguments?.getString("historyId") ?: ""
            HistoryDetailScreen(navController, historyId)
        }
    }
}
