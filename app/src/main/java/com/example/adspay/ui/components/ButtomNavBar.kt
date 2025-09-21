package com.example.adspay.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.adspay.navigation.Screen

@Composable
fun BottomNavBar(
    navController: NavController,
    items: List<Screen>
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    val isOdd = items.size % 2 != 0
    val middleIndex = if (isOdd) items.size / 2 else -1

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        items.forEachIndexed { index, screen ->
            val isSelected = currentRoute == screen.route

            val iconModifier = if (isOdd && index == middleIndex) {
                Modifier.size(48.dp)
            } else {
                Modifier.size(24.dp)
            }

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    if (screen.iconResId != null) {
                        Icon(
                            painter = painterResource(id = screen.iconResId),
                            contentDescription = screen.label,
                            tint = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = iconModifier
                        )
                    } else if (screen.iconVector != null) {
                        Icon(
                            imageVector = screen.iconVector,
                            contentDescription = screen.label,
                            tint = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = iconModifier
                        )
                    }
                },
                label = {
                    Text(
                        text = screen.label,
                        color = if (isSelected)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }
    }
}