package com.example.adspay.ui.components

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LifecycleOwner
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetectorOptions
import androidx.camera.view.PreviewView
import java.util.concurrent.Executors

class FaceDetectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FaceDetectionScreen()
        }
    }
}

@OptIn(ExperimentalGetImage::class)
@Composable
fun FaceDetectionScreen() {
    val context = LocalContext.current
    val lifecycleOwner = context as LifecycleOwner
    val resultText = remember { mutableStateOf("Wajah belum terdeteksi") }
    val previewView = remember { PreviewView(context) }

    LaunchedEffect(Unit) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val cameraProvider = cameraProviderFuture.get()

        val preview = Preview.Builder().build().also {
            it.setSurfaceProvider(previewView.surfaceProvider)
        }

        val faceDetector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .build()
        )

        val executor = Executors.newSingleThreadExecutor()
        val imageAnalyzer = ImageAnalysis.Builder().build().also {
            it.setAnalyzer(executor) { imageProxy ->
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)
                    faceDetector.process(inputImage)
                        .addOnSuccessListener { faces ->
                            resultText.value = if (faces.isNotEmpty()) {
                                "Wajah terdeteksi: ${faces.size}"
                            } else {
                                "Tidak ada wajah"
                            }
                        }
                        .addOnFailureListener {
                            resultText.value = "Error mendeteksi wajah"
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                } else {
                    imageProxy.close()
                }
            }
        }

        cameraProvider.unbindAll()
        cameraProvider.bindToLifecycle(
            lifecycleOwner,
            CameraSelector.DEFAULT_FRONT_CAMERA,
            preview,
            imageAnalyzer
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )
        Text(
            text = resultText.value,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}
