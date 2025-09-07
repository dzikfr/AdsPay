package com.example.adspay.screens.register

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.adspay.services.RegisterService

@Composable
fun RegisterOtpScreen(navController: NavController, registerService: RegisterService, phone: String) {
    var otp by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = otp,
                onValueChange = { otp = it },
                label = { Text("Kode OTP") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))
            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            val res = registerService.validateOtp(phone, otp)
                            if (res.resp_code == "00" && res.data != null) {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "registration_token",
                                    res.data.registrationToken
                                )
                                navController.navigate("registerForm/$phone")
                            } else {
                                snackbarHostState.showSnackbar(res.resp_message)
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Error: ${e.localizedMessage}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Verifikasi OTP")
            }
        }
    }
}
