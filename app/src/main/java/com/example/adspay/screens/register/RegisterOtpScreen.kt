package com.example.adspay.screens.register

import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adspay.R
import kotlinx.coroutines.launch
import com.example.adspay.services.RegisterService
import com.example.adspay.ui.components.OtpInput
import kotlinx.coroutines.delay
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun RegisterOtpScreen(navController: NavController, registerService: RegisterService, phone: String) {
    var otp by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // State untuk resend
    var timer by remember { mutableIntStateOf(60) } // 60 detik
    var isResendEnabled by remember { mutableStateOf(false) }

    // Countdown jalan otomatis
    LaunchedEffect(key1 = timer) {
        if (timer > 0) {
            delay(1000)
            timer--
        } else {
            isResendEnabled = true
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.mobile_phone_1),
                contentDescription = "OTP Logo",
                modifier = Modifier.size(240.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "OTP Verification",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Enter the OTP sent to +$phone",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            OtpInput(
                otp = otp,
                onOtpChange = { otp = it },
                otpLength = 6
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
                Text(
                    text = "Verify",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.surface
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Resend section
            if (isResendEnabled) {
                TextButton(
                    onClick = {
                        coroutineScope.launch {
                            try {
                                val res = registerService.sendOtp(phone)
                                if (res.resp_code == "00") {
                                    snackbarHostState.showSnackbar("OTP resent to $phone")
                                    timer = 60
                                    isResendEnabled = false
                                } else {
                                    snackbarHostState.showSnackbar(res.resp_message)
                                }
                            } catch (e: Exception) {
                                snackbarHostState.showSnackbar("Error: ${e.localizedMessage}")
                            }
                        }
                    }
                ) {
                    Text(
                        "Resend OTP",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            textDecoration = TextDecoration.Underline
                        )
                    )
                }
            } else {
                Text(
                    text = "Resend available in $timer s",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
