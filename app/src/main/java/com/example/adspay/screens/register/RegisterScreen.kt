package com.example.adspay.screens.register

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.adspay.R
import kotlinx.coroutines.launch
import com.example.adspay.services.RegisterService
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterPhoneScreen(navController: NavController, registerService: RegisterService) {
    var phoneNumber by remember { mutableStateOf("") }

    // daftar prefix negara
    val prefixes = listOf("62", "60", "65", "66", "81") // contoh: Indonesia, Malaysia, SG, Thailand, Jepang?
    var expanded by remember { mutableStateOf(false) }
    var selectedPrefix by remember { mutableStateOf(prefixes.first()) }

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
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
                modifier = Modifier
                    .size(240.dp)
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
                text = "We will send you an OTP to verify your phone number.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Enter Phone Number",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // row gabungan select box prefix + nomor hp
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Select box untuk prefix
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.width(100.dp)
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedPrefix,
                        onValueChange = {},
                        label = { Text("Code") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        prefixes.forEach { prefix ->
                            DropdownMenuItem(
                                text = { Text(prefix) },
                                onClick = {
                                    selectedPrefix = prefix
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // Input nomor hp
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it.filter { ch -> ch.isDigit() } }, // hanya angka
                    label = { Text("Phone Number") },
                    placeholder = { Text("ex: 812345...") },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            val normalizedNumber = phoneNumber.trimStart('0') // buang leading 0
                            val fullNumber = selectedPrefix + normalizedNumber
                            val encodedNumber = URLEncoder.encode(fullNumber, StandardCharsets.UTF_8.toString())

                            val res = registerService.sendOtp(fullNumber)
                            if (res.resp_code == "00") {
                                navController.navigate("registerOtp/$encodedNumber")
                            } else {
                                snackbarHostState.showSnackbar(res.resp_message)
                            }
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Error: ${e.localizedMessage}")
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Get OTP",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surface
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Arrow Right",
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
        }
    }
}
