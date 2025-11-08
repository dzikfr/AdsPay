package com.example.adspay.screens

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adspay.ui.components.IconMenuGrid
import com.example.adspay.ui.components.MenuItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.adspay.services.ApiClient
import com.example.adspay.models.user.UserData
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import com.example.adspay.utils.SessionManager
import kotlinx.coroutines.launch
import com.example.adspay.services.UserService
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.material.icons.filled.ContentCopy
import androidx. compose. foundation. background
import com.example.adspay.constant.*

@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val clipboardManager: ClipboardManager = LocalClipboardManager.current

    val retrofit = remember { ApiClient.create(context, ApiConfig.BASE_URL) }
    val apiService = remember { retrofit.create(UserService::class.java) }

    val coroutineScope = rememberCoroutineScope()
    var userData by remember { mutableStateOf<UserData?>(null) }
    var isSaldoVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val profile = apiService.getUserProfile("Bearer ${SessionManager(context).getAccessToken()}")
                userData = profile.data
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    val menuItemsPembayaran = listOf(
        MenuItem("Top Up", Icons.Default.Add) { navController.navigate("topup_guide") },
        MenuItem("Transfer Sesama", Icons.Default.AccountBalanceWallet) { navController.navigate("internal_transfer") },
        MenuItem("Transfer Bank", Icons.Default.AssuredWorkload) { navController.navigate("bank_transfer") }
    )

    val menuItemsPembayaran2 = listOf(
        MenuItem("BPJS", Icons.Default.HealthAndSafety) {},
        MenuItem("PBB", Icons.Default.LocationCity) {},
        MenuItem("Listrik", Icons.Default.FlashOn) {}
    )

    val menuItemsPembayaran3 = listOf(
        MenuItem("Pendidikan", Icons.Default.School) {},
        MenuItem("Shopping", Icons.Default.LocalGroceryStore) {}
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(20.dp)
                            .background(MaterialTheme.colorScheme.surfaceTint, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(20.dp)
                            .background(MaterialTheme.colorScheme.surfaceTint, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(28.dp)
                            .background(MaterialTheme.colorScheme.surfaceTint, RoundedCornerShape(4.dp))
                    )
                }
            }
        } else {
            userData?.let { user ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Hi, ${user.fullName}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = user.phoneNumber,
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                IconButton(onClick = {
                                    clipboardManager.setText(AnnotatedString(user.phoneNumber))
                                }) {
                                    Icon(
                                        imageVector = Icons.Default.ContentCopy,
                                        contentDescription = "Copy",
                                        tint = MaterialTheme.colorScheme.surface,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }

                            Text(
                                text = if (user.registrationStatus == "UNREGISTERED") "❌ Unregistered" else "✅ Registered",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.surface
                                )
                            )
                        }

                        Divider(modifier = Modifier.padding(vertical = 12.dp))

                        Text(
                            text = "Saldo Kamu",
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = if (isSaldoVisible) "Rp ${"%,d".format(user.saldo)}" else "Rp ******",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            IconButton(onClick = { isSaldoVisible = !isSaldoVisible }) {
                                Icon(
                                    imageVector = if (isSaldoVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Saldo"
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Semua grid menu ikut scroll juga
        IconMenuGrid(title = "Pembayaran", items = menuItemsPembayaran)
        IconMenuGrid(title = "Tagihan", items = menuItemsPembayaran2)
//        IconMenuGrid(title = "Tagihan lainnya", items = menuItemsPembayaran3)
//        IconMenuGrid(title = "Tagihan", items = menuItemsPembayaran2)
//        IconMenuGrid(title = "Tagihan lainnya", items = menuItemsPembayaran3)
    }
}
