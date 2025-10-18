package com.example.adspay.screens.register

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.content.FileProvider
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.adspay.services.UserService
import okhttp3.MultipartBody
import com.example.adspay.utils.SessionManager
import com.example.adspay.services.ApiClient
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KycFormScreen(navController: NavController) {
    val sessionManager = SessionManager(LocalContext.current)
    val token = sessionManager.getAccessToken()
    val context = LocalContext.current
    val baseUrl = "http://38.47.94.165:3123"
    val retrofit = remember { ApiClient.create(context, baseUrl) }
    val apiService = remember { retrofit.create(UserService::class.java) }
    var showCamera by remember { mutableStateOf(false) }


    var nik by remember { mutableStateOf("") }
    var fullName by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var placeOfBirth by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("LAKI-LAKI") }
    var religion by remember { mutableStateOf("ISLAM") }
    var maritalStatus by remember { mutableStateOf("") }
    var job by remember { mutableStateOf("") }

    val religions = listOf("ISLAM", "KRISTEN", "KATOLIK", "HINDU", "BUDDHA", "KONGHUCU")

    var showDatePicker by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    var showSuccessDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val ktpBitmap = remember { mutableStateOf<Bitmap?>(null) }
    val selfieBitmap = remember { mutableStateOf<Bitmap?>(null) }

    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    val tmpKtpUri = remember {
        val file = File(context.cacheDir, "ktp_temp.jpg")
        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    val selfieLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
        if (it != null) {
            selfieBitmap.value = it
        } else {
            Toast.makeText(context, "Gagal ambil gambar selfie", Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (!granted) {
            Toast.makeText(context, "Izin kamera diperlukan untuk mengambil foto", Toast.LENGTH_SHORT).show()
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                dateOfBirth = "%02d-%02d-%04d".format(day, month + 1, year)
                showDatePicker = false
            },
            1990, 0, 1
        ).show()
    }

    if (showCamera) {
        KtpCameraScreen(
            onCapture = { bitmap ->
                // tampilkan hasil ke layar form
                ktpBitmap.value = bitmap
                showCamera = false

                // jalankan OCR otomatis
                val image = InputImage.fromBitmap(bitmap, 0)
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        val text = visionText.text
                        Log.d("OCR_RESULT", text)

                        val nikRegex = Regex("\\b\\d{16}\\b")
                        val nameRegex = Regex("Nama\\s*:?\\s*([A-Z ]+)", RegexOption.IGNORE_CASE)

                        val nikFound = nikRegex.find(text)?.value ?: ""
                        val nameFound = nameRegex.find(text)?.groupValues?.get(1)?.trim() ?: ""

                        if (nikFound.isNotEmpty()) nik = nikFound
                        if (nameFound.isNotEmpty()) fullName = nameFound
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "OCR gagal: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                    }
            },
            onClose = { showCamera = false }
        )
        return
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        OutlinedTextField(value = nik, onValueChange = { nik = it }, label = { Text("NIK") })
        OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("Nama Lengkap") })
        OutlinedTextField(value = placeOfBirth, onValueChange = { placeOfBirth = it }, label = { Text("Tempat Lahir") })

        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = {},
            label = { Text("Tanggal Lahir") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = { showDatePicker = true }) {
            Text("Pilih Tanggal")
        }

        OutlinedTextField(value = address, onValueChange = { address = it }, label = { Text("Alamat") })

        Text("Jenis Kelamin")
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = gender == "LAKI-LAKI", onClick = { gender = "LAKI-LAKI" })
            Text("Laki-Laki", modifier = Modifier.padding(end = 16.dp))
            RadioButton(selected = gender == "PEREMPUAN", onClick = { gender = "PEREMPUAN" })
            Text("Perempuan")
        }

        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = religion,
                onValueChange = {},
                label = { Text("Agama") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                religions.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(item) },
                        onClick = {
                            religion = item
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(value = maritalStatus, onValueChange = { maritalStatus = it }, label = { Text("Status Perkawinan") })
        OutlinedTextField(value = job, onValueChange = { job = it }, label = { Text("Pekerjaan") })

        Spacer(modifier = Modifier.height(12.dp))

        Text("Foto KTP:")
        CaptureImageButton(bitmap = ktpBitmap.value) {
            showCamera = true
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text("Foto Selfie:")
        CaptureImageButton(bitmap = selfieBitmap.value) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
                selfieLauncher.launch(null)
            } else {
                permissionLauncher.launch(android.Manifest.permission.CAMERA)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            coroutineScope.launch {
                try {
                    val ktpFile = bitmapToTempFile(context, ktpBitmap.value, "ktp.jpg")
                    val selfieFile = bitmapToTempFile(context, selfieBitmap.value, "selfie.jpg")

                    val metadataJson = JSONObject().apply {
                        put("nik", nik)
                        put("fullName", fullName)
                        put("placeOfBirth", placeOfBirth)
                        put("dateOfBirth", dateOfBirth)
                        put("address", address)
                        put("gender", gender)
                        put("religion", religion)
                        put("maritalStatus", maritalStatus)
                        put("job", job)
                    }

                    val selfiePart = MultipartBody.Part.createFormData(
                        "photo_selfie",
                        selfieFile.name,
                        selfieFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )

                    val ktpPart = MultipartBody.Part.createFormData(
                        "photo_ktp",
                        ktpFile.name,
                        ktpFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    )

                    val metadataBody = metadataJson.toString()
                        .toRequestBody("application/json".toMediaTypeOrNull())

                    apiService.submitKyc("Bearer $token", selfiePart, ktpPart, metadataBody)
                    showSuccessDialog = true
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(context, "Gagal kirim KYC", Toast.LENGTH_SHORT).show()
                }
            }
        }, enabled = ktpBitmap.value != null && selfieBitmap.value != null) {
            Text("Submit KYC")
        }

        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = { showSuccessDialog = false },
                title = { Text("Berhasil Registrasi") },
                text = { Text("Tunggu proses verifikasi selesai.") },
                confirmButton = {
                    TextButton(onClick = { showSuccessDialog = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Composable
fun CaptureImageButton(
    bitmap: Bitmap?,
    onClickLaunch: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Captured image",
                modifier = Modifier
                    .size(200.dp)
                    .padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = onClickLaunch,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (bitmap == null) "Ambil Foto" else "Ambil Ulang Foto")
        }
    }
}

fun bitmapToTempFile(context: Context, bitmap: Bitmap?, fileName: String): File {
    val file = File(context.cacheDir, fileName)
    FileOutputStream(file).use { out ->
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, out)
    }
    return file
}