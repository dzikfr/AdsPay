package com.example.adspay.screens.register

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.adspay.models.auth.PartialRegisterData

@Composable
fun RegisterFormScreen(
    navController: NavController,
    initialPhone: String,
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

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
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Nama Depan") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nama Belakang") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        if (firstName.isBlank() || email.isBlank() || password.length < 6) {
                            snackbarHostState.showSnackbar("Lengkapi data (password min 6 karakter).")
                            return@launch
                        }

                        val partial = PartialRegisterData(
                            firstName = firstName,
                            lastName = lastName,
                            email = email,
                            password = password,
                            phoneNumber = initialPhone
                        )

                        navController.currentBackStackEntry?.savedStateHandle?.set("partial_register", partial)
                        navController.currentBackStackEntry?.savedStateHandle?.set("registration_token", registrationToken)

                        navController.navigate("registerPin")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lanjut ke Set PIN")
            }
        }
    }
}
