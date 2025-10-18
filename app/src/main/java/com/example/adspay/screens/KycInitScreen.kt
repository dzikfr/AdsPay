package com.example.adspay.screens

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.adspay.ui.components.FaceDetectionActivity
import com.example.adspay.ui.components.TextRecognitionActivity


@Composable
fun KycInitScreen(navController: NavController) {
    val context = LocalContext.current
    Column(modifier = Modifier.padding(16.dp)) {
        Text("Verifikasi Wajah")

        Button(onClick = {
            val intent = Intent(context, FaceDetectionActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Mulai Deteksi Wajah")
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            val intent = Intent(context, TextRecognitionActivity::class.java)
            context.startActivity(intent)
        }) {
            Text("Foto & OCR KTP")
        }

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("kyc")
        }) {
            Text("Lanjut ke Form KYC")
        }
    }
}
