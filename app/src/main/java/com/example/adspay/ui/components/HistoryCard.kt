package com.example.adspay.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.adspay.models.history.History
import java.text.NumberFormat
import java.util.*

@Composable
fun HistoryCard(
    history: History,
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
            Row {
                // 🔹 Ganti Coil dengan NetworkImage bawaan
                NetworkImage(
                    url = history.iconUrl,
                    contentDescription = history.actionName,
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = history.actionName,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = history.dateTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            val formattedAmount = NumberFormat
                .getCurrencyInstance(Locale("id", "ID"))
                .format(history.amount)

            Text(
                text = if (history.isIncome) "+$formattedAmount" else "-$formattedAmount",
                color = if (history.isIncome)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
