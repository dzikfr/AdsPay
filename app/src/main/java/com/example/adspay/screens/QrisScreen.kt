package com.example.adspay.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.mlkit.vision.common.InputImage
import androidx.compose.ui.unit.sp
import com.google.mlkit.vision.barcode.BarcodeScanning
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.compose.ui.viewinterop.AndroidView
import androidx.camera.core.CameraSelector


@OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun QrisScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var qrText by remember { mutableStateOf("") }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val source = InputImage.fromFilePath(context, it)
            scanQrCode(source) { result ->
                qrText = result
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("QRIS Scanner", fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                imagePickerLauncher.launch("image/*")
            }
        ) {
            Icon(Icons.Default.Image, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Pilih Gambar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        CameraPreview(modifier = Modifier.weight(1f)) { qrResult ->
            qrText = qrResult
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text("Hasil QR:", fontWeight = FontWeight.Bold)
        Text(qrText.ifEmpty { "Belum ada hasil" }, color = Color.Green)
    }
}

@OptIn(androidx.camera.core.ExperimentalGetImage::class)
@Composable
fun CameraPreview(modifier: Modifier = Modifier, onQrScanned: (String) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val analyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()

                analyzer.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                    @OptIn(androidx.camera.core.ExperimentalGetImage::class)
                    val mediaImage = imageProxy.image
                    if (mediaImage != null) {
                        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                        scanQrCode(image) { result ->
                            onQrScanned(result)
                        }
                    }
                    imageProxy.close()
                }


                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        analyzer
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}

fun scanQrCode(image: InputImage, onResult: (String) -> Unit) {
    val scanner = BarcodeScanning.getClient()

    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            for (barcode in barcodes) {
                val value = barcode.rawValue
                if (value != null) {
                    onResult(value)
                    break
                }
            }
        }
        .addOnFailureListener {
            Log.e("QRScanner", "Failed to scan: ${it.localizedMessage}")
            onResult("Gagal scan QR")
        }
}
