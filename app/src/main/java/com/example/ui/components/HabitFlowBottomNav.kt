package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.InsertChart
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.InsertChart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.model.NavigationTab
import com.example.ui.theme.PrimaryBlue

@Composable
fun HabitFlowBottomNav(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 8.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp,
            modifier = Modifier
                .navigationBarsPadding()
                .height(64.dp)
                .testTag("bottom_nav_bar")
        ) {
            // Tab 1: Home
            val isHome = currentTab == NavigationTab.HOME
            NavigationBarItem(
                selected = isHome,
                onClick = { onTabSelected(NavigationTab.HOME) },
                icon = {
                    Icon(
                        imageVector = if (isHome) Icons.Filled.Home else Icons.Outlined.Home,
                        contentDescription = "Home",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Home",
                        fontSize = 12.sp,
                        fontWeight = if (isHome) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    indicatorColor = PrimaryBlue.copy(alpha = 0.14f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_home")
            )

            // Tab 2: Analytics
            val isAnalytics = currentTab == NavigationTab.ANALYTICS
            NavigationBarItem(
                selected = isAnalytics,
                onClick = { onTabSelected(NavigationTab.ANALYTICS) },
                icon = {
                    Icon(
                        imageVector = if (isAnalytics) Icons.Filled.InsertChart else Icons.Outlined.InsertChart,
                        contentDescription = "Analytics",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Analytics",
                        fontSize = 12.sp,
                        fontWeight = if (isAnalytics) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    indicatorColor = PrimaryBlue.copy(alpha = 0.14f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_analytics")
            )

            // Tab 3: Settings
            val isSettings = currentTab == NavigationTab.SETTINGS
            NavigationBarItem(
                selected = isSettings,
                onClick = { onTabSelected(NavigationTab.SETTINGS) },
                icon = {
                    Icon(
                        imageVector = if (isSettings) Icons.Filled.Settings else Icons.Outlined.Settings,
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = "Settings",
                        fontSize = 12.sp,
                        fontWeight = if (isSettings) FontWeight.Bold else FontWeight.Medium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    indicatorColor = PrimaryBlue.copy(alpha = 0.14f),
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.testTag("nav_item_settings")
            )
        }
    }
}
