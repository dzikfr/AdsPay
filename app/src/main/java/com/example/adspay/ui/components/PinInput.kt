package com.example.adspay.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun PinInput(
    pin: String,
    onPinChange: (String) -> Unit,
    pinLength: Int = 6
) {
    val focusRequester = remember { FocusRequester() }

    BasicTextField(
        value = pin,
        onValueChange = {
            if (it.length <= pinLength && it.all { ch -> ch.isDigit() }) {
                onPinChange(it)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        modifier = Modifier.focusRequester(focusRequester), // cukup ini
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(pinLength) { index ->
                    val char = pin.getOrNull(index)?.toString() ?: ""
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.medium
                            )
                            .clickable {
                                focusRequester.requestFocus() // klik kotak → kasih fokus ke BasicTextField
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (char.isNotEmpty()) "•" else "",
                            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    )
}
