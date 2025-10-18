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

@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    val retrofit = remember { ApiClient.create(context, "http://38.47.94.165:3123/") }
    val apiService = remember { retrofit.create(UserService::class.java) }

    val coroutineScope = rememberCoroutineScope()
    var userData by remember { mutableStateOf<UserData?>(null) }
    var isSaldoVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val profile = apiService.getUserProfile("Bearer ${SessionManager(context).getAccessToken()}")
                userData = profile.data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val menuItemsPembayaran = listOf(
        MenuItem("Pulsa", Icons.Default.Phone) {
            navController.navigate("pulsa")
        },
        MenuItem("Token", Icons.Default.Face) {
            navController.navigate("token")
        },
        MenuItem("Settings", Icons.Default.Settings) {
            navController.navigate("settings")
        } ,
        MenuItem("Settings", Icons.Default.Settings) {
            navController.navigate("settings")
        },
        MenuItem("Settings", Icons.Default.Settings) {
            navController.navigate("settings")
        }
    )

    val menuItemsPembayaran2 = listOf(
        MenuItem("Pulsa", Icons.Default.Phone) {
            navController.navigate("pulsa")
        },
        MenuItem("Token", Icons.Default.Face) {
            navController.navigate("token")
        },
        MenuItem("Settings", Icons.Default.Settings) {
            navController.navigate("settings")
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        userData?.let { user ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = user.phoneNumber,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = if (user.registrationStatus == "UNREGISTERED") "❌ Unregistered" else "✅ Registered",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (user.registrationStatus == "UNREGISTERED")
                            MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.primary
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {

                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Your Balance",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isSaldoVisible) "Rp ${"%,d".format(user.saldo)}" else "Rp ******",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        IconButton(onClick = { isSaldoVisible = !isSaldoVisible }) {
                            Icon(
                                imageVector = if (isSaldoVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle Saldo",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            IconMenuGrid(title = "Pembayaran", items = menuItemsPembayaran)
            IconMenuGrid(title = "Pembayaran", items = menuItemsPembayaran)
            IconMenuGrid(title = "Pembayaran", items = menuItemsPembayaran)
            IconMenuGrid(title = "Tagihan", items = menuItemsPembayaran2)
        }
    }
}
