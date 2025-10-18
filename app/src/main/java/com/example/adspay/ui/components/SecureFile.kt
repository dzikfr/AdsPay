package com.example.adspay.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import androidx.compose.ui.platform.LocalContext
import com.example.adspay.utils.SessionManager
import androidx.compose.material3.Text
import java.net.URL

@Composable
fun SecureFile(
    url: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val token = sessionManager.getAccessToken()

    var loading by remember(url, token) { mutableStateOf(true) }
    var error by remember(url, token) { mutableStateOf<Throwable?>(null) }

    val bitmapState = produceState<Bitmap?>(initialValue = null, url, token) {
        try {
            loading = true
            error = null
            val bitmap = withContext(Dispatchers.IO) {
                val connection = (URL(url).openConnection() as HttpURLConnection).apply {
                    setRequestProperty("Authorization", "Bearer $token")
                }
                connection.connect()
                connection.inputStream.use { BitmapFactory.decodeStream(it) }
            }
            value = bitmap
        } catch (e: Exception) {
            error = e
            value = null
        } finally {
            loading = false
        }
    }

    when {
        loading -> Text("Loading foto…")
        error != null -> Text("Gagal load foto (${error?.message})")
        bitmapState.value != null -> Image(
            bitmap = bitmapState.value!!.asImageBitmap(),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
        )
    }
}
