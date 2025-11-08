package com.example.adspay.screens.transfer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adspay.models.transfer.TransferBankPayload
import com.example.adspay.services.ApiClient
import com.example.adspay.services.TransferInterface
import com.example.adspay.utils.NotificationHelper
import com.example.adspay.utils.SessionManager
import kotlinx.coroutines.launch
import com.example.adspay.constant.ApiConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankConfirmScreen(
    navController: NavController,
    bankCode: String,
    accountNumber: String,
    amount: String,
    remark: String
) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val retrofit = remember { ApiClient.create(context, ApiConfig.BASE_URL) }
    val service = remember { retrofit.create(TransferInterface::class.java) }
    val coroutineScope = rememberCoroutineScope()

    var pin by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("Konfirmasi Transfer") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = pin,
                onValueChange = { pin = it },
                label = { Text("Masukkan PIN") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (pin.isNotBlank()) {
                        coroutineScope.launch {
                            isLoading = true
                            try {
                                val token = "Bearer ${session.getAccessToken()}"
                                val payload = TransferBankPayload(
                                    bankCode = bankCode,
                                    accountNumber = accountNumber,
                                    amount = amount.toDouble(),
                                    description = remark,
                                    pin = pin
                                )
                                val res = service.transferBank(token, payload)

                                if (res.resp_code?.trim() == "00" && res.data != null) {
                                    navController.navigate("bankResult/${res.data.transactionRef}")
                                } else {
                                    NotificationHelper.showNotification(
                                        context,
                                        "Gagal",
                                        res.resp_message ?: "Tidak ada pesan"
                                    )
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                                NotificationHelper.showNotification(
                                    context,
                                    "Error",
                                    "Terjadi kesalahan saat transfer"
                                )
                            } finally {
                                isLoading = false
                            }
                        }
                    } else {
                        NotificationHelper.showNotification(
                            context,
                            "PIN Kosong",
                            "Masukkan PIN terlebih dahulu"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kirim")
            }

            if (isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        }
    }
}
