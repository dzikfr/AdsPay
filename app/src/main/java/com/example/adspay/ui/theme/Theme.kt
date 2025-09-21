package com.example.adspay.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

//private val LightColors = lightColorScheme(
//    primary = Purple40,
//    secondary = PurpleGrey40,
//    tertiary = Pink40,
//    background =  WhiteBg,
//    surface = Color(0xFFFFFFFF)
//)
//
//private val DarkColors = darkColorScheme(
//    primary = Purple80,
//    secondary = PurpleGrey80,
//    tertiary = Pink80,
//    background = BlackBg,
//    surface = Color(0xFF1E1E1E)
//)

@Composable
fun Theme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (useDarkTheme) {
        darkColorScheme(
            primary = Purple80,
            secondary = PurpleGrey80,
            tertiary = Pink80,
            background = BlackBg,
            surface =  Color(0xFF1E1E1E)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF5409DA),
            secondary = Color(0xFF4E71FF),
            tertiary = Color(0xFF8DD8FF),
            background = Color(0xFFBBFBFF),
            surface = Color(0xFFFFFFFF)
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}
