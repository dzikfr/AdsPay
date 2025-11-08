package com.example.adspay.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adspay.utils.SessionManager
import com.example.adspay.services.ApiClient
import com.example.adspay.models.user.UserDetailData
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import kotlinx.coroutines.launch
import com.example.adspay.services.UserService
import com.example.adspay.ui.components.SecureFile
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import com.example.adspay.ui.components.HeaderBlob
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import com.example.adspay.constant.ApiConfig

@Composable
fun ProfileScreen(
    isDarkMode: Boolean,
    toggleTheme: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    val retrofit = remember { ApiClient.create(context, ApiConfig.BASE_URL) }
    val apiService = remember { retrofit.create(UserService::class.java) }

    val coroutineScope = rememberCoroutineScope()
    var userData by remember { mutableStateOf<UserDetailData?>(null) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                val profile = apiService.getUserDetail("Bearer ${sessionManager.getAccessToken()}")
                userData = profile.data
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Konfirmasi Logout") },
            text = { Text("Apakah Anda yakin ingin keluar dari akun?") },
            confirmButton = {
                TextButton(onClick = {
                    sessionManager.clearSession()
                    showLogoutDialog = false
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                }) { Text("Logout") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Batal") }
            }
        )
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Sign out", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HeaderBlob()

            userData?.let { user ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                        if (!user.selfieUrl.isNullOrBlank()) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                SecureFile(
                                    url = ApiConfig.BASE_URL + user.selfieUrl,
                                    contentDescription = "Foto Profil",
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(CircleShape)
                                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        Text(
                            text = user.fullName ?: user.phoneNumber,
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = if (user.registrationStatus == "REGISTERED") "Online" else "Belum Terdaftar",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (user.registrationStatus == "REGISTERED")
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.height(24.dp))

                        if (user.registrationStatus == "UNREGISTERED") {
                            Button(onClick = {
                                navController.navigate("kyc")
                            }) {
                                Text("Verifikasi Diri")
                            }
                        }

                        ProfileRow(label = "Username", value = user.phoneNumber)
                        ProfileRow(label = "NIK", value = user.nik.orEmpty())
                        ProfileRow(label = "Tempat Lahir", value = user.placeOfBirth.orEmpty())
                        ProfileRow(label = "Tanggal Lahir", value = user.dob.orEmpty())
                        ProfileRow(label = "Pekerjaan", value = user.job.orEmpty())
                        ProfileRow(label = "Status KYC", value = user.kycStatus.orEmpty())
                        Spacer(Modifier.height(80.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.DarkMode else Icons.Default.LightMode,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(Modifier.width(8.dp))
                            Switch(
                                checked = isDarkMode,
                                onCheckedChange = { toggleTheme() },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = MaterialTheme.colorScheme.primary,
                                    uncheckedThumbColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
