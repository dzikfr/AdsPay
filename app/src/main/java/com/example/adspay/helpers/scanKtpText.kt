package com.example.adspay.helpers

import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

//fun scanKtpText(image: InputImage, onResult: (Map<String, String>) -> Unit) {
//    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
//    recognizer.process(image)
//        .addOnSuccessListener { visionText ->
//            val result = mutableMapOf<String, String>()
//            val fullText = visionText.text
//
//            // Normalisasi teks agar mudah diparsing
//            val normalized = fullText.replace("\\n", " ").replace("  +".toRegex(), " ").uppercase()
//
//            // Regex pattern untuk ambil data penting
//            val nikRegex = Regex("NIK\\s*:?\\s*([0-9]{8,20})")
//            val namaRegex = Regex("NAMA\\s*:?\\s*([A-Z ]+)")
//            val ttlRegex = Regex("TEMPAT/TGL LAHIR\\s*:?\\s*([A-Z ]+),\\s*([0-9]{2}-[0-9]{2}-[0-9]{4})")
//            val alamatRegex = Regex("ALAMAT\\s*:?\\s*([A-Z0-9 ,.]+)")
//
//            nikRegex.find(normalized)?.let { result["nik"] = it.groupValues[1].trim() }
//            namaRegex.find(normalized)?.let { result["nama"] = it.groupValues[1].trim() }
//            ttlRegex.find(normalized)?.let {
//                result["ttl"] = it.groupValues[1].trim() + ", " + it.groupValues[2].trim()
//            }
//            alamatRegex.find(normalized)?.let { result["alamat"] = it.groupValues[1].trim() }
//
//            onResult(result)
//        }
//        .addOnFailureListener {
//            it.printStackTrace()
//            onResult(emptyMap())
//        }
//}
fun scanKtpText(image: InputImage, onResult: (Map<String, String>) -> Unit) {
    val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    recognizer.process(image)
        .addOnSuccessListener { visionText ->
            val result = mutableMapOf<String, String>()
            val lines = visionText.text.lines()

            for (line in lines) {
                val cleanLine = line.trim().uppercase()

                if (cleanLine.contains("NIK")) {
                    result["nik"] = line.replace("NIK", "", ignoreCase = true).trim()
                }
                if (cleanLine.contains("NAMA")) {
                    result["nama"] = line.replace("NAMA", "", ignoreCase = true).trim()
                }
                if (cleanLine.contains("TEMPAT") || cleanLine.contains("TGL LAHIR") || cleanLine.contains("TTL")) {
                    result["ttl"] = line.replace("TTL", "", ignoreCase = true).trim()
                }
            }

            onResult(result)
        }
        .addOnFailureListener {
            it.printStackTrace()
            onResult(emptyMap())
        }
}
