package com.example.adspay.utils

import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

object BiometricHelper {

    fun isBiometricAvailable(context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        val authenticators = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            BiometricManager.Authenticators.BIOMETRIC_STRONG or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        } else {
            BiometricManager.Authenticators.BIOMETRIC_WEAK or
                    BiometricManager.Authenticators.DEVICE_CREDENTIAL
        }

        return biometricManager.canAuthenticate(authenticators) ==
                BiometricManager.BIOMETRIC_SUCCESS
    }

    /**
     * Hybrid-safe: works even if BiometricPrompt only accepts FragmentActivity
     */
    fun authenticate(
        activity: ComponentActivity,
        onSuccess: () -> Unit,
        onFailed: () -> Unit,
        onError: ((Int, CharSequence) -> Unit)? = null
    ) {
        // Cast ComponentActivity ke FragmentActivity jika bisa
        val fragmentActivity = when (activity) {
            is FragmentActivity -> activity
            else -> {
                // fallback – buat dummy FragmentActivity context (biar gak crash)
                return onError?.invoke(-1, "This device cannot start biometric prompt") ?: Unit
            }
        }

        val executor = ContextCompat.getMainExecutor(fragmentActivity)

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Autentikasi Biometrik")
            .setSubtitle("Gunakan sidik jari atau PIN/pola/password")
            .setAllowedAuthenticators(
                BiometricManager.Authenticators.BIOMETRIC_STRONG or
                        BiometricManager.Authenticators.DEVICE_CREDENTIAL
            )
            .build()

        val biometricPrompt = BiometricPrompt(
            fragmentActivity,  // ✅ Sekarang aman
            executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    onSuccess()
                }

                override fun onAuthenticationFailed() {
                    onFailed()
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    onError?.invoke(errorCode, errString)
                }
            }
        )

        biometricPrompt.authenticate(promptInfo)
    }
}
