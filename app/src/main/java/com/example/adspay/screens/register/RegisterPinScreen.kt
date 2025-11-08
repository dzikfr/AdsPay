package com.example.adspay.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.adspay.models.auth.PartialRegisterData
import com.example.adspay.models.auth.RegisterRequest
import com.example.adspay.services.RegisterService
import com.example.adspay.ui.components.PinInput
import retrofit2.HttpException

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
            Text(
                text = "Set Your 6-Digit PIN",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            PinInput(
                pin = pin,
                onPinChange = { pin = it },
                pinLength = 6
            )

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (partial == null || registrationToken == null) {
                            snackbarHostState.showSnackbar("Data registrasi tidak lengkap.")
                            return@launch
                        }
                        if (pin.length < 6) {
                            snackbarHostState.showSnackbar("PIN harus 6 digit.")
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
                            if (res.resp_code == "00") {
                                snackbarHostState.showSnackbar("Registrasi berhasil!")
                                navController.navigate("login") {
                                    popUpTo("registerPhone") { inclusive = true }
                                }
                            } else {
                                snackbarHostState.showSnackbar(res.resp_message)
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Gagal registrasi: ${e.localizedMessage}")
                        } catch (e: HttpException){
                            snackbarHostState.showSnackbar("Server Error: ${e.message()}")
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
