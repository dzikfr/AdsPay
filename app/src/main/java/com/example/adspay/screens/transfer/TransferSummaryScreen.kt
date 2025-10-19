package com.example.adspay.screens.transfer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferSummaryScreen(
    navController: NavController,
    receiverName: String,
    amount: String,
    note: String
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ringkasan Transfer") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("✅ Transfer Berhasil", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(24.dp))
            Text("Kepada: $receiverName", style = MaterialTheme.typography.bodyMedium)
            Text("Jumlah: Rp$amount", style = MaterialTheme.typography.bodyMedium)
            if (note.isNotBlank()) {
                Text("Catatan: $note", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Kembali ke Beranda")
            }
        }
    }
}
