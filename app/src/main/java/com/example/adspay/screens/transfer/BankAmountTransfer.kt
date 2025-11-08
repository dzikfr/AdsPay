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
import com.example.adspay.models.transfer.DataPreviewBankTransfer
import com.example.adspay.models.transfer.TransferBankPreviewPayload
import com.example.adspay.services.ApiClient
import com.example.adspay.services.TransferInterface
import com.example.adspay.utils.SessionManager
import kotlinx.coroutines.launch
import com.example.adspay.constant.ApiConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankAmountScreen(
    navController: NavController,
    bankCode: String,
    bankName: String,
    accountNumber: String,
    accountName: String
) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val retrofit = remember { ApiClient.create(context, ApiConfig.BASE_URL) }
    val service = remember { retrofit.create(TransferInterface::class.java) }
    val coroutineScope = rememberCoroutineScope()

    var amount by remember { mutableStateOf("") }
    var remark by remember { mutableStateOf("") }
    var preview by remember { mutableStateOf<DataPreviewBankTransfer?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Scaffold(topBar = { TopAppBar(title = { Text("Input Nominal") }) }) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                label = { Text("Nominal Transfer") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = remark,
                onValueChange = { remark = it },
                label = { Text("Catatan (opsional)") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (amount.isNotBlank()) {
                        coroutineScope.launch {
                            isLoading = true
                            try {
                                val token = "Bearer ${session.getAccessToken()}"
                                val payload = TransferBankPreviewPayload(
                                    bankCode = bankCode,
                                    accountNumber = accountNumber,
                                    amount = amount.toDouble(),
                                    description = remark.ifEmpty { null }
                                )
                                val res = service.previewTransferBank(token, payload)
                                preview = res.data
                            } catch (e: Exception) {
                                e.printStackTrace()
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Lihat Preview")
            }

            if (isLoading) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())

            preview?.let { p ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Bank: ${p.bankName}")
                        Text("A/N: ${p.accountName}")
                        Text("Nominal: Rp${p.amount}")
                        Text("Biaya Bank: Rp${p.bankFee}")
                        Text("Total Debit: Rp${p.totalDebit}")
                    }
                }

                Button(
                    onClick = {
                        navController.navigate(
                            "bankConfirm/${bankCode}/${accountNumber}/${p.amount}/${remark}"
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Konfirmasi")
                }
            }
        }
    }
}
