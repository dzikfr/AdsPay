package com.example.adspay.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adspay.models.history.HistoryItem
import com.example.adspay.services.ApiClient
import com.example.adspay.services.HistoryService
import com.example.adspay.ui.components.HistoryCard
import kotlinx.coroutines.launch
import com.example.adspay.utils.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {

    val context = LocalContext.current

    val retrofit = remember { ApiClient.create(context, "http://38.47.94.165:3123/") }
    val historyService = remember { retrofit.create(HistoryService::class.java) }

    var histories by remember { mutableStateOf<List<HistoryItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val token = "Bearer ${SessionManager(context).getAccessToken()}"
                val result = historyService.getUserHistory(token)
                histories = result.data.content
                errorMessage = null
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = "Gagal memuat riwayat"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Riwayat Transaksi") })
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                isLoading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                errorMessage != null -> Text(
                    text = errorMessage ?: "Terjadi kesalahan",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
                histories.isEmpty() -> Text(
                    text = "Belum ada riwayat",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(histories) { history ->
                        HistoryCard(history = history) {
                            navController.navigate("historyDetail/${history.transactionCode}")
                        }
                    }
                }
            }
        }
    }
}
