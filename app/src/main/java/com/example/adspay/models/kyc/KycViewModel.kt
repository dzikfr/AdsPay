package com.example.adspay.models.kyc

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class KycViewModel : ViewModel() {
    var nik by mutableStateOf("")
    var fullName by mutableStateOf("")
    var address by mutableStateOf("")

    fun fromOCR(text: String) {
        val lines = text.lines()
        lines.forEach { line ->
            when {
                line.contains("NIK") -> nik = extractNumber(line)
                line.contains("Nama") -> fullName = extractName(line)
                line.contains("Alamat") -> address = extractAddress(line)
            }
        }
    }

    private fun extractNumber(line: String): String {
        return Regex("\\d+").find(line)?.value ?: ""
    }

    private fun extractName(line: String): String {
        return line.replace("Nama", "", ignoreCase = true).trim()
    }

    private fun extractAddress(line: String): String {
        return line.replace("Alamat", "", ignoreCase = true).trim()
    }
}
