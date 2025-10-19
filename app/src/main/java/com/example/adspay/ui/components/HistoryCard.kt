package com.example.adspay.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.adspay.models.history.HistoryItem
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryCard(
    history: HistoryItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = when (history.type) {
                        "TOPUP" -> "Top-up Saldo"
                        "TRANSFER_OUT" -> "Transfer Keluar"
                        "TRANSFER_IN" -> "Transfer Masuk"
                        else -> history.type
                    },
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = history.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                val formattedDate = try {
                    val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                    val date = parser.parse(history.createdAt)
                    SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("id", "ID")).format(date!!)
                } catch (e: Exception) {
                    history.createdAt
                }

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            val formattedAmount = NumberFormat
                .getCurrencyInstance(Locale("id", "ID"))
                .format(history.amount)

            Text(
                text = if (history.direction == "IN") "+$formattedAmount" else "-$formattedAmount",
                color = if (history.direction == "IN")
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
