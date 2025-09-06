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
import com.example.adspay.models.history.History
//import com.example.adspay.services.HistoryService
import com.example.adspay.ui.components.HistoryCard
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(navController: NavController) {
    val dummyHistories = listOf(
        History(
            id = "1",
            type = "payment",
            iconUrl = "https://img.icons8.com/color/48/money.png",
            actionName = "Pembayaran Pulsa",
            dateTime = "2025-08-25 14:30",
            amount = 50000.0,
            isIncome = false,
            isSuccess = true
        ),
        History(
            id = "2",
            type = "topup",
            iconUrl = "https://img.icons8.com/color/48/money-bag.png",
            actionName = "Top Up Saldo",
            dateTime = "2025-08-25 10:15",
            amount = 100000.0,
            isIncome = true,
            isSuccess = true
        ),
        History(
            id = "3",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "4",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "5",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "6",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "7",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "8",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "9",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "10",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "11",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "12",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "13",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "14",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        ),
        History(
            id = "15",
            type = "transfer",
            iconUrl = "https://img.icons8.com/color/48/bank-transfer.png",
            actionName = "Transfer ke Budi",
            dateTime = "2025-08-24 19:00",
            amount = 75000.0,
            isIncome = false,
            isSuccess = false
        )
    )
    val context = LocalContext.current
//    val historyService = remember { HistoryService(context) }

//    var histories by remember { mutableStateOf<List<History>>(emptyList()) }
    var histories by remember { mutableStateOf(dummyHistories) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
//                val result = historyService.getHistories()
                val result = dummyHistories
                histories = result
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
            TopAppBar(
                title = { Text("Riwayat Transaksi") }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage ?: "Terjadi kesalahan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                histories.isEmpty() -> {
                    Text(
                        text = "Belum ada riwayat",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(histories) { history ->
                            HistoryCard(history = history) {
                                navController.navigate("historyDetail/${history.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}
