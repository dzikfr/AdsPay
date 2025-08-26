package com.example.adspay.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.adspay.models.promotion.PromotionCategory
import com.example.adspay.services.PromotionService
import com.example.adspay.ui.components.PromotionCarousel
import kotlinx.coroutines.launch
import androidx.compose.ui.platform.LocalContext
import com.example.adspay.models.history.History
import kotlin.String
import com.example.adspay.models.promotion.Promotion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PromotionScreen(navController: NavController) {
    val context = LocalContext.current
    val promotionService = remember { PromotionService(context) }
    var categories by remember { mutableStateOf<List<PromotionCategory>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    val dummyPromotion = listOf(
        PromotionCategory(
            name = "Event",
            promotions = listOf(
                Promotion(
                    id = "String",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRb04Qmhrxv5TDxE60ec3-Gyo_FsnuULS59Tw&s",
                    title= "String",
                    description= null,
                    link= null
                ),
                Promotion(
                    id = "String",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRb04Qmhrxv5TDxE60ec3-Gyo_FsnuULS59Tw&s",
                    title= "String",
                    description= null,
                    link= null
                ),
                Promotion(
                    id = "String",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRb04Qmhrxv5TDxE60ec3-Gyo_FsnuULS59Tw&s",
                    title= "String",
                    description= null,
                    link= null
                ),
            ),
        ),
        PromotionCategory(
            name = "Promo",
            promotions = listOf(
                Promotion(
                    id = "String",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRb04Qmhrxv5TDxE60ec3-Gyo_FsnuULS59Tw&s",
                    title= "String",
                    description= null,
                    link= null
                ),
                Promotion(
                    id = "String",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRb04Qmhrxv5TDxE60ec3-Gyo_FsnuULS59Tw&s",
                    title= "String",
                    description= null,
                    link= null
                ),
                Promotion(
                    id = "String",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRb04Qmhrxv5TDxE60ec3-Gyo_FsnuULS59Tw&s",
                    title= "String",
                    description= null,
                    link= null
                ),
                Promotion(
                    id = "String",
                    imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRb04Qmhrxv5TDxE60ec3-Gyo_FsnuULS59Tw&s",
                    title= "String",
                    description= null,
                    link= null
                ),
            ),
        ),
    );


    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
//                categories = promotionService.getPromotions()
                categories = dummyPromotion
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Promosi") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            categories.forEach { category ->
                PromotionCarousel(
                    title = category.name,
                    promotions = category.promotions,
                    isFullWidth = category.name.contains("Event", true),
                    itemHeight = if (category.name.contains("Event", true)) 180 else 140,
                    onClick = { promo ->
                        // bisa navigate ke promo detail
                        navController.navigate("promotionDetail/${promo.id}")
                    }
                )
            }
        }
    }
}
