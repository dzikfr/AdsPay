package com.example.adspay.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.adspay.models.auth.PartialRegisterData
import com.example.adspay.models.auth.RegisterRequest
import com.example.adspay.services.RegisterService

@Composable
fun RegisterPinScreen(
    navController: NavController,
    registerService: RegisterService
) {
    var pin by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    val partial: PartialRegisterData? =
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<PartialRegisterData>("partial_register")

    val registrationToken: String? =
        navController.previousBackStackEntry
            ?.savedStateHandle
            ?.get<String>("registration_token")

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {
            OutlinedTextField(
                value = pin,
                onValueChange = { new -> pin = new.filter { it.isDigit() }.take(6) },
                label = { Text("PIN (6 digit)") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (partial == null || registrationToken == null) {
                            snackbarHostState.showSnackbar("Data registrasi tidak lengkap.")
                            return@launch
                        }

                        val req = RegisterRequest(
                            firstName = partial.firstName,
                            lastName = partial.lastName,
                            email = partial.email,
                            phoneNumber = partial.phoneNumber,
                            password = partial.password,
                            pin = pin
                        )

                        try {
                            val res = registerService.registerUser(req, registrationToken)
                            if (res.success) {
                                snackbarHostState.showSnackbar("Registrasi berhasil!")
                                navController.navigate("login") {
                                    popUpTo("registerPhone") { inclusive = true }
                                }
                            } else {
                                snackbarHostState.showSnackbar(res.message)
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Gagal registrasi: ${e.localizedMessage}")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Selesaikan Registrasi")
            }
        }
    }
}
