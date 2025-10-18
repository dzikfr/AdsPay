package com.example.adspay.utils

import android.graphics.*
import com.google.mlkit.vision.text.Text

data class KtpFields(
    val nik: String = "",
    val name: String = "",
    val placeOfBirth: String = "",
    val dateOfBirth: String = "",
    val address: String = "",
    val job: String = "",
    val maritalStatus: String = ""
)

object KtpOcrUtils {

    fun Bitmap.toGrayscale(): Bitmap {
        val grayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(grayscale)
        val paint = Paint().apply {
            colorFilter = ColorMatrixColorFilter(ColorMatrix().apply { setSaturation(0f) })
        }
        canvas.drawBitmap(this, 0f, 0f, paint)
        return grayscale
    }

    fun parseKtpText(result: Text): KtpFields {
        val textBlocks = result.textBlocks
        val allLines = textBlocks.flatMap { it.lines }

        val nik = findNik(allLines)
        val name = findValueLabel(listOf("name", "nama", "neme", "nema"), allLines)
        val (placeOfBirth, dateOfBirth) = findPlaceAndDateOfBirth(allLines)
//        val address = findAddress(allLines)
        val address = findValueLabel(listOf("alamat"), allLines)
        val job = findValueLabel(listOf("pekerjaan", "kerjaan", "kerja"), allLines)
        val maritalStatus = findMaritalStatus(allLines)

        return KtpFields(
            nik = nik ?: "",
            name = name ?: "",
            placeOfBirth = placeOfBirth ?: "",
            dateOfBirth = dateOfBirth ?: "",
            address = address ?: "",
            job = job ?: "",
            maritalStatus = maritalStatus ?: ""
        )
    }

    private fun findNik(lines: List<Text.Line>): String? {
        return lines
            .map { it.text.replace("\\s".toRegex(), "") }
            .firstOrNull { it.matches(Regex("^\\d{16}$")) }
    }

    private fun findMaritalStatus(lines: List<Text.Line>): String? {
        val labelLine = lines.firstOrNull { it.text.lowercase().contains("perkawinan") }
        val labelBox = labelLine?.boundingBox ?: return null

        labelLine.text.split(":").getOrNull(1)?.trim()?.let { inlineValue ->
            if (inlineValue.isNotEmpty()) {
                return mapMaritalStatus(inlineValue)
            }
        }

        val candidate = lines
            .filter { it.boundingBox != null && isRightOrSameLine(labelBox, it.boundingBox!!) }
            .map { it.text.trim().replace(":", "") }
            .firstOrNull {
                it.length in 3..30 &&
                        it.any { c -> c.isLetter() }
            }

        return mapMaritalStatus(candidate)
    }

    private fun mapMaritalStatus(raw: String?): String? {
        if (raw == null) return null
        val text = normalizeName(raw)

        return when {
            text.contains("BELUM") -> "BELUM KAWIN"
            text.contains("CERAI") && text.contains("HIDUP") -> "CERAI HIDUP"
            text.contains("CERAI") && text.contains("MATI") -> "CERAI MATI"
            text.contains("KAWIN") -> "KAWIN"
            else -> null
        }
    }

    private fun findPlaceAndDateOfBirth(lines: List<Text.Line>): Pair<String?, String?> {
        for (line in lines) {
            val text = line.text.uppercase().replace(":", "").trim()
            if (text.contains("TEMPAT") && text.contains("LAHIR")) {
                Regex("([A-Z\\s]+)[,\\s]+(\\d{2}-\\d{2}-\\d{4})").find(text)?.let { match ->
                    val place = match.groupValues[1].trim()
                    val date = match.groupValues[2].trim()
                    return Pair(place, date)
                }
            }
        }
        // Fallback: cari baris yang match "TANGERANG, 01-01-2001"
        for (line in lines) {
            val parts = line.text.uppercase().split(",")
            if (parts.size == 2 && parts[1].trim().matches(Regex("\\d{2}-\\d{2}-\\d{4}"))) {
                return Pair(parts[0].trim(), parts[1].trim())
            }
        }
        return Pair(null, null)
    }

    private fun findAddress(lines: List<Text.Line>): String? {
        for ((i, line) in lines.withIndex()) {
            val text = line.text.lowercase()
            if (text.contains("alamat")) {
                val parts = mutableListOf<String>()
                parts.add(line.text.trim())

                for (j in 1..3) {
                    val next = lines.getOrNull(i + j)?.text?.trim() ?: continue
                    if (
                        next.contains("rt", ignoreCase = true) ||
                        next.contains("rw", ignoreCase = true) ||
                        next.contains("kel", ignoreCase = true) ||
                        next.contains("desa", ignoreCase = true) ||
                        next.contains("kecamatan", ignoreCase = true)
                    ) {
                        parts.add(next)
                    }
                }

                return parts.joinToString(", ") { it.replace(":", "").uppercase() }
            }
        }
        return null
    }

    fun findValueLabel(labelKeywords: List<String>, lines: List<Text.Line>): String? {
        val labelLine = lines.firstOrNull { it.text.lowercase().containsAny(labelKeywords) }
        val labelBox = labelLine?.boundingBox ?: return null

        labelLine.text.split(":").getOrNull(1)?.trim()?.let { inlineValue ->
            if (inlineValue.isNotEmpty()) {
                return normalizeName(inlineValue)
            }
        }

        return lines
            .filter { it.boundingBox != null && isRightOrSameLine(labelBox, it.boundingBox!!) }
            .map { it.text.trim().replace(":", "") }
            .firstOrNull {
                it.length in 3..50 &&
                        !it.containsAny(labelKeywords) &&
                        it.any { c -> c.isLetter() }
            }
            ?.let { normalizeName(it) }
    }


    private fun isRightOrSameLine(label: Rect, candidate: Rect): Boolean {
        val labelHeight = label.height()
        val isRight = candidate.left >= label.right
        val isSameLine = candidate.centerY() >= label.top - labelHeight &&
                candidate.centerY() <= label.bottom + labelHeight
        return isRight && isSameLine
    }

    private fun normalizeName(text: String): String =
        text.uppercase()
            .replace("1", "I")
            .replace("0", "O")
            .replace("6", "G")
            .replace("8", "B")
            .replace("5", "S")
            .trim()

    private fun String.containsAny(keywords: List<String>, ignoreCase: Boolean = true): Boolean {
        return keywords.any { this.contains(it, ignoreCase) }
    }
}
