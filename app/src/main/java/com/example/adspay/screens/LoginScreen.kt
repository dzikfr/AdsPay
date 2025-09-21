package com.example.adspay.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.example.adspay.services.AuthService
import com.example.adspay.utils.SessionManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

@Composable
fun LoginScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val sessionManager = remember { SessionManager(context) }
    val coroutineScope = rememberCoroutineScope()

    // Base URL untuk Keycloak (beda dengan backend API)
    val authService = remember { AuthService(context, "https://auth.devmj.web.id/") }
    val clientId = "adspay-mobile-client"

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Sign-in",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.VisibilityOff
                    else Icons.Filled.Visibility

                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(imageVector = image, contentDescription = null)
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    coroutineScope.launch {
                        try {
                            val response = authService.login(
                                clientId = clientId,
                                username = username,
                                password = password
                            )
                            sessionManager.saveAuthSession(
                                accessToken = response.accessToken,
                                refreshToken = response.refreshToken,
                                expiresIn = response.expiresIn
                            )
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        } catch (e: HttpException) {
                            snackbarHostState.showSnackbar("Login gagal: ${e.message()}")
                            e.printStackTrace()
                        } catch (e: IOException) {
                            snackbarHostState.showSnackbar("Tidak dapat terhubung ke server.")
                            e.printStackTrace()
                        } catch (e: Exception) {
                            snackbarHostState.showSnackbar("Terjadi kesalahan: ${e.localizedMessage}")
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 16.dp, horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sign-in",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.surface
                        )
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward, // panah kanan bawaan
                        contentDescription = "Arrow Right",
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            val signUpText = buildAnnotatedString {
                append("Don't have an account? ")
                pushStringAnnotation(tag = "SIGNUP", annotation = "signup")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Sign-up")
                }
                pop()
            }

            ClickableText(
                text = signUpText,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                onClick = { offset ->
                    signUpText.getStringAnnotations(tag = "SIGNUP", start = offset, end = offset)
                        .firstOrNull()?.let {
                            navController.navigate("registerPhone")
                        }
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            val resetText = buildAnnotatedString {
                append("Forgot Password? ")
                pushStringAnnotation(tag = "RESET", annotation = "reset")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Reset Password")
                }
                pop()
            }

            ClickableText(
                text = resetText,
                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
                onClick = { offset ->
                    resetText.getStringAnnotations(tag = "RESET", start = offset, end = offset)
                        .firstOrNull()?.let {
                            navController.navigate("registerPhone")
                        }
                }
            )
        }
    }
}
