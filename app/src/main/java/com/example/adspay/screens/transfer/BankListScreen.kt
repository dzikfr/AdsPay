package com.example.adspay.screens.transfer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.adspay.models.transfer.GetBankDataResponse
import com.example.adspay.services.ApiClient
import com.example.adspay.services.TransferInterface
import com.example.adspay.utils.SessionManager
import kotlinx.coroutines.launch
import com.example.adspay.constant.ApiConfig

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BankListScreen(navController: NavController) {
    val context = LocalContext.current
    val session = remember { SessionManager(context) }
    val retrofit = remember { ApiClient.create(context, ApiConfig.BASE_URL) }
    val service = remember { retrofit.create(TransferInterface::class.java) }

    var banks by remember { mutableStateOf<List<GetBankDataResponse>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val token = "Bearer ${session.getAccessToken()}"
                val res = service.getBankList(token)
                banks = res.data ?: emptyList()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(
            title = { Text("Pilih Bank")  },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        )
    }) { padding ->
        if (isLoading) {
            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
        } else {
            LazyColumn(modifier = Modifier.padding(padding)) {
                items(banks) { bank ->
                    ListItem(
                        headlineContent = { Text(bank.bankName) },
                        supportingContent = { Text(bank.bankCode) },
                        modifier = Modifier.clickable {
                            navController.navigate("bankInquiry/${bank.bankCode}/${bank.bankName}")
                        }
                    )
                    Divider()
                }
            }
        }
    }
}
