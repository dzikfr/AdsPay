package com.example.adspay.screens.transfer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adspay.models.transfer.DataInquiryBank
import com.example.adspay.models.transfer.InquiryBankPayload
import com.example.adspay.services.ApiClient
import com.example.adspay.services.TransferInterface
import com.example.adspay.utils.NotificationHelper
import com.example.adspay.utils.SessionManager
import kotlinx.coroutines.launch
import com.example.adspay.constant.ApiConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankInquiryScreen(navController: NavController, bankCode: String, bankName: String) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val retrofit = remember { ApiClient.create(context, ApiConfig.BASE_URL) }
    val service = remember { retrofit.create(TransferInterface::class.java) }
    val coroutineScope = rememberCoroutineScope()

    var accountNumber by remember { mutableStateOf("") }
    var result by remember { mutableStateOf<DataInquiryBank?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(bankName) },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = accountNumber,
                onValueChange = { accountNumber = it },
                label = { Text("Nomor Rekening") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (accountNumber.isNotBlank()) {
                        coroutineScope.launch { // ✅ coroutine yang benar di Compose
                            isLoading = true
                            val payload = InquiryBankPayload(bankCode, accountNumber)
                            try {
                                val token = "Bearer ${session.getAccessToken()}"
                                val res = service.inquiryBank(token, payload)
                                result = res.data
                            } catch (e: Exception) {
                                e.printStackTrace()
                                NotificationHelper.showNotification(
                                    context,
                                    "Gagal",
                                    "Nomor rekening tidak ditemukan"
                                )
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cek Rekening")
            }

            if (isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            result?.let { data ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Nama Pemilik: ${data.accountName}")
                        Text("Nomor Rekening: ${data.accountNumber}")
                    }
                }

                Button(
                    onClick = {
                        navController.navigate(
                            "bankAmount/${bankCode}/${bankName}/${data.accountNumber}/${data.accountName}"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Lanjut")
                }
            }
        }
    }
}
