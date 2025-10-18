package com.example.adspay.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp


@Composable
fun HeaderBlob() {
    val primary = MaterialTheme.colorScheme.primary

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
    ) {
        val w = size.width
        val h = size.height

        val path = Path().apply {
            moveTo(0f, 0f)
            lineTo(0f, h * 0.7f)
            cubicTo(
                w * 0.25f, h,
                w * 0.75f, h * 0.4f,
                w,         h * 0.8f
            )
            lineTo(w, 0f)
            close()
        }

        drawPath(path = path, color = primary)
    }
}
