package com.example.adspay.screens.transfer

import androidx.compose.foundation.layout.*
import com.example.adspay.utils.NotificationHelper
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adspay.models.transfer.*
import com.example.adspay.services.ApiClient
import com.example.adspay.services.TransferInterface
import com.example.adspay.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternalTransferScreen(navController: NavController) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val retrofit = remember { ApiClient.create(context, "http://38.47.94.165:3123/") }
    val transferService = remember { retrofit.create(TransferInterface::class.java) }

    var phoneNumber by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }

    var inquiryResult by remember { mutableStateOf<DataInquiry?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transfer Internal") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Nomor Telepon Penerima") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Phone
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        coroutineScope.launch {
                            if (phoneNumber.isNotBlank()) {
                                isLoading = true
                                try {
                                    val token = "Bearer ${sessionManager.getAccessToken()}"
                                    val response = transferService.inquiryNumber(
                                        token,
                                        InquiryPayload(phoneNumber)
                                    )
                                    inquiryResult = response.data
                                    errorMessage = null
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    errorMessage = "Nomor tidak ditemukan atau gagal inquiry"
                                } finally {
                                    isLoading = false
                                }
                            }
                        }
                    }
                )
            )

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            inquiryResult?.let { data ->
                if (data.exists) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text("Penerima ditemukan:", style = MaterialTheme.typography.titleSmall)
                            Text(data.fullName?.takeIf { it.isNotBlank() } ?: data.phoneNumber, style = MaterialTheme.typography.titleMedium)
                            Text(data.phoneNumber, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = amount,
                        onValueChange = { amount = it },
                        label = { Text("Jumlah Transfer") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    OutlinedTextField(
                        value = note,
                        onValueChange = { note = it },
                        label = { Text("Catatan (opsional)") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                if (amount.isNotBlank()) {
                                    isLoading = true
                                    try {
                                        val token = "Bearer ${sessionManager.getAccessToken()}"
                                        val payload = TransferPayload(
                                            receiverPhone = data.phoneNumber,
                                            amount = amount.toDouble(),
                                            note = note
                                        )
                                        val res = transferService.internalTransfer(token, payload)
                                        if (res.resp_code == "00") {
                                            val receiverLabel = data.fullName?.takeIf { it.isNotBlank() } ?: data.phoneNumber
                                            NotificationHelper.showNotification(
                                                context = context,
                                                title = "Transfer Berhasil",
                                                message = "Kamu mengirim Rp${amount} ke $receiverLabel"
                                            )
                                            navController.navigate(
                                                "transferSummary/${data.fullName}/${amount}/${note}"
                                            )
                                        } else {
                                            errorMessage = res.resp_message
                                            delay(2000)
                                            NotificationHelper.showNotification(
                                                context = context,
                                                title = "Transfer Gagal",
                                                message = "${res.resp_message}"
                                            )
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        errorMessage = "Transfer gagal dilakukan"
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Kirim Sekarang")
                    }
                }
            }
        }
    }
}
