package com.example.adspay.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adspay.models.history.HistoryDetail
import com.example.adspay.services.ApiClient
import com.example.adspay.services.HistoryService
import com.example.adspay.utils.SessionManager
import kotlinx.coroutines.launch
import com.example.adspay.constant.ApiConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryDetailScreen(navController: NavController, historyId: String) {
    val context = LocalContext.current
    val retrofit = remember { ApiClient.create(context, ApiConfig.BASE_URL) }
    val service = remember { retrofit.create(HistoryService::class.java) }
    val sessionManager = remember { SessionManager(context) }

    var detail by remember { mutableStateOf<HistoryDetail?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val token = "Bearer ${sessionManager.getAccessToken()}"
                val response = service.getTransactionDetail(token, historyId)
                detail = response.data
                errorMessage = null
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Gagal memuat detail transaksi"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Transaksi") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                errorMessage != null -> Text(
                    text = errorMessage ?: "Terjadi kesalahan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
                detail != null -> {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text("Kode Transaksi: ${detail!!.transactionCode}", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Tipe: ${detail!!.type}")
                        Text("Arah: ${detail!!.direction}")
                        Text("Jumlah: Rp ${detail!!.amount}")
                        Text("Saldo Setelah: Rp ${detail!!.balanceAfter}")
                        Text("Kanal: ${detail!!.channel}")
                        Text("Referensi: ${detail!!.referenceId}")
                        Text("Sumber Eksternal: ${detail!!.externalSource}")
                        Text("Status: ${detail!!.status}")
                        Text("Deskripsi: ${detail!!.description}")
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Dibuat: ${detail!!.createdAt}")
                        Text("Diperbarui: ${detail!!.updatedAt}")
                    }
                }
                else -> Text(
                    text = "Data tidak ditemukan",
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}
