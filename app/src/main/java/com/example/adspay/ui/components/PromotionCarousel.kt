package com.example.adspay.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.example.adspay.models.promotion.Promotion

@Composable
fun PromotionCarousel(
    title: String,
    promotions: List<Promotion>,
    itemHeight: Int = 140,
    isFullWidth: Boolean = false,
    onClick: (Promotion) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(promotions) { promo ->
                Card(
                    modifier = Modifier
                        .then(
                            if (isFullWidth) Modifier.fillParentMaxWidth() else Modifier.width(260.dp)
                        )
                        .height(itemHeight.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    onClick = { onClick(promo) }
                ) {
                    NetworkImage(
                        url = promo.imageUrl,
                        contentDescription = promo.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
