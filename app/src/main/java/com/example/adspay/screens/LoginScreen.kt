package com.example.adspay.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.example.adspay.services.AuthService
import com.example.adspay.utils.SessionManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val sessionManager = remember { SessionManager(context) }
    val coroutineScope = rememberCoroutineScope()

    // Base URL untuk Keycloak (beda dengan backend API)
    val authService = remember { AuthService(context, "https://auth.devmj.web.id/") }
    val clientId = "adspay-mobile-client"

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Login", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            val response = authService.login(
                                clientId = clientId,
                                username = username,
                                password = password
                            )
                            sessionManager.saveAuthSession(
                                accessToken = response.accessToken,
                                refreshToken = response.refreshToken,
                                expiresIn = response.expiresIn
                            )
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } catch (e: HttpException) {
                            snackbarHostState.showSnackbar("Login gagal: ${e.message()}")
                            e.printStackTrace()
                        } catch (e: IOException) {
                            snackbarHostState.showSnackbar("Tidak dapat terhubung ke server.")
                            e.printStackTrace()
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Terjadi kesalahan: ${e.localizedMessage}")
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
        }
    }
}
