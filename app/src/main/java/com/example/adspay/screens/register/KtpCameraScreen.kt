package com.example.adspay.screens.register

import java.io.File

import android.Manifest
import androidx.compose.material.icons.Icons
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import android.graphics.BitmapFactory
import java.io.InputStream
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors

@Composable
fun KtpCameraScreen(onCapture: (Bitmap) -> Unit, onClose: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val executor = remember { Executors.newSingleThreadExecutor() }

    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }

    LaunchedEffect(Unit) {
        if (!hasPermission) {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        } else {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

            val capture = ImageCapture.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .build()

            val selector = CameraSelector.DEFAULT_BACK_CAMERA

            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview, capture)
            imageCapture = capture
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.fillMaxSize()
        )

        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val overlayColor = Color.Black.copy(alpha = 0.6f)
            drawRect(color = overlayColor)

            val frameWidth = size.width * 0.9f
            val frameHeight = frameWidth / 1.585f
            val left = (size.width - frameWidth) / 2
            val top = (size.height - frameHeight) / 2

            drawRect(
                color = Color.Transparent,
                topLeft = androidx.compose.ui.geometry.Offset(left, top),
                size = Size(frameWidth, frameHeight),
                blendMode = androidx.compose.ui.graphics.BlendMode.Clear
            )

            drawRect(
                color = Color.White.copy(alpha = 0.5f),
                topLeft = androidx.compose.ui.geometry.Offset(left, top),
                size = Size(frameWidth, frameHeight),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3f)
            )
        }

        Button(
            onClick = {
                val capture = imageCapture ?: return@Button
                val outputOptions = ImageCapture.OutputFileOptions.Builder(
                    File(context.cacheDir, "temp_ktp_capture.jpg")
                ).build()

                capture.takePicture(
                    outputOptions,
                    executor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            try {
                                val file = File(context.cacheDir, "temp_ktp_capture.jpg")
                                val bitmap = BitmapFactory.decodeFile(file.absolutePath)

                                val exif = androidx.exifinterface.media.ExifInterface(file.absolutePath)
                                val orientation = exif.getAttributeInt(
                                    androidx.exifinterface.media.ExifInterface.TAG_ORIENTATION,
                                    androidx.exifinterface.media.ExifInterface.ORIENTATION_NORMAL
                                )

                                val rotated = when (orientation) {
                                    androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(bitmap, 90)
                                    androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(bitmap, 180)
                                    androidx.exifinterface.media.ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(bitmap, 270)
                                    else -> bitmap
                                }

                                val cropped = cropToKtpFrame(rotated)
                                onCapture(cropped)
                            } catch (e: Exception) {
                                Log.e("CameraX", "Failed to process image: ${e.message}")
                            }
                        }

                        override fun onError(exc: ImageCaptureException) {
                            Log.e("CameraX", "Capture failed: ${exc.message}")
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 32.dp)
        ) {
            Text("Ambil Foto KTP")
        }

        // Tombol close
        IconButton(
            onClick = onClose,
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp)
        ) {
            Icon(Icons.Default.Cancel, contentDescription = "Tutup", tint = Color.White)
        }
    }
}

private fun cropToKtpFrame(bitmap: Bitmap): Bitmap {
    val targetRatio = 1.585f
    val width = bitmap.width
    val height = bitmap.height
    val bitmapRatio = width.toFloat() / height.toFloat()

    var newWidth = width
    var newHeight = height

    if (bitmapRatio > targetRatio) {
        newWidth = (height * targetRatio).toInt()
    } else {
        newHeight = (width / targetRatio).toInt()
    }

    val xOffset = (width - newWidth) / 2
    val yOffset = (height - newHeight) / 2

    return Bitmap.createBitmap(bitmap, xOffset, yOffset, newWidth, newHeight)
}


private fun rotateBitmap(bitmap: Bitmap, degrees: Int): Bitmap {
    if (degrees == 0) return bitmap
    val matrix = android.graphics.Matrix()
    matrix.postRotate(degrees.toFloat())
    return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
}
