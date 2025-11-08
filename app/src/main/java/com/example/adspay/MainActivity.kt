package com.example.adspay

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.adspay.ui.components.BottomNavBar
import com.example.adspay.ui.components.AppNavigation
import com.example.adspay.navigation.Screen
import com.example.adspay.ui.theme.*
import androidx.compose.ui.platform.LocalContext
import com.example.adspay.utils.SessionManager
import com.example.adspay.services.AuthService
import com.example.adspay.utils.BiometricHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.widget.Toast
import com.example.adspay.ui.components.BiometricPromptActivity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import com.example.adspay.constant.ApiConfig

class MainActivity : ComponentActivity() {

    private val startDestinationState = mutableStateOf("login")
    private lateinit var biometricLauncher: androidx.activity.result.ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        biometricLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                startDestinationState.value = "home"
            } else {
                startDestinationState.value = "login"
            }
            showMainContent()
        }

        super.onCreate(savedInstanceState)

        val sessionManager = SessionManager(this)

        // ✅ Cek apakah user sudah login
        if (sessionManager.isLoggedIn()) {
            // ✅ Kalau login, cek apakah biometrik tersedia
            if (BiometricHelper.isBiometricAvailable(this)) {
                val intent = Intent(this, BiometricPromptActivity::class.java)
                biometricLauncher.launch(intent)
            } else {
                startDestinationState.value = "home"
                showMainContent()
            }

        } else {
            startDestinationState.value = "login"
            showMainContent()
        }
    }

    private fun showMainContent() {
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }

            Theme(useDarkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(
                        isDarkMode = isDarkMode,
                        toggleTheme = { isDarkMode = !isDarkMode },
                        startDestination = startDestinationState.value
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    isDarkMode: Boolean,
    toggleTheme: () -> Unit,
    startDestination: String
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val navScreens = listOf(Screen.Home, Screen.Promotion, Screen.Qris, Screen.History, Screen.Profile)
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val showBar = navScreens.any { it.route == currentRoute }

    // ✅ Auto-refresh token setiap 30 detik kalau hampir kadaluarsa
    LaunchedEffect(Unit) {
        val authService = AuthService(context, ApiConfig.BASE_URL)
        while (true) {
            val remaining = sessionManager.getExpiresInSeconds()
            if (remaining in 1..60) {
                val refreshToken = sessionManager.getRefreshToken()
                if (!refreshToken.isNullOrEmpty()) {
                    try {
                        val response = authService.refreshToken(refreshToken)
                        response.data?.let { data ->
                            sessionManager.saveAuthSession(
                                accessToken = data.accessToken,
                                refreshToken = data.refreshToken,
                                expiresIn = data.expiresIn
                            )
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        sessionManager.clearSession()
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                }
            }
            delay(30_000)
        }
    }

    Scaffold(
        bottomBar = {
            if (showBar) {
                BottomNavBar(navController = navController, items = navScreens)
            }
        },
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) { padding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            isDarkMode = isDarkMode,
            toggleTheme = toggleTheme,
            startDestination = startDestination
        )
    }
}
