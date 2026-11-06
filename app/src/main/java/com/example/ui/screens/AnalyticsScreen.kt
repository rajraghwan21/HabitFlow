package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.HabitFlowUiState
import com.example.ui.components.HabitIconHelper
import com.example.ui.model.AchievementBadge
import com.example.ui.model.AnalyticsTimeFilter
import com.example.ui.model.DayChartData
import com.example.ui.model.HabitPerformanceUi
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.ActionPink
import com.example.ui.theme.HighlightYellow
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PurpleLavender

@Composable
fun AnalyticsScreen(
    uiState: HabitFlowUiState,
    onFilterChange: (AnalyticsTimeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDayData by remember { mutableStateOf<DayChartData?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("analytics_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Title & Filter Header
        item {
            Column {
                Text(
                    text = "Analytics & Insights",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Track your habit momentum, streaks & achievements",
                    fontSize = 13.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Filter Pills: Weekly vs Monthly
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                FilterChip(
                    selected = uiState.timeFilter == AnalyticsTimeFilter.WEEKLY,
                    onClick = { onFilterChange(AnalyticsTimeFilter.WEEKLY) },
                    label = { Text("Weekly View (7 Days)", fontWeight = FontWeight.Bold) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Equalizer,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryBlue,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("filter_weekly")
                )

                FilterChip(
                    selected = uiState.timeFilter == AnalyticsTimeFilter.MONTHLY,
                    onClick = { onFilterChange(AnalyticsTimeFilter.MONTHLY) },
                    label = { Text("Monthly View (30 Days)", fontWeight = FontWeight.Bold) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.TrendingUp,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = PrimaryBlue,
                        selectedLabelColor = Color.White,
                        selectedLeadingIconColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("filter_monthly")
                )
            }
        }

        // Top 4 Summary Cards Grid
        item {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Overall Completion Rate
                    StatMetricCard(
                        title = "Completion Rate",
                        value = "${uiState.overallCompletionPercentage}%",
                        subtitle = "Last 7 days",
                        icon = Icons.Default.Percent,
                        accentColor = PrimaryBlue,
                        modifier = Modifier.weight(1f)
                    )

                    // Total Completed Habits
                    StatMetricCard(
                        title = "Total Done",
                        value = "${uiState.totalCompletionsAllTime}",
                        subtitle = "Habits logged",
                        icon = Icons.Default.CheckCircle,
                        accentColor = MintGreen,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Current Streak
                    StatMetricCard(
                        title = "Current Streak",
                        value = "${uiState.currentStreak} Days",
                        subtitle = "Active streak",
                        icon = Icons.Default.LocalFireDepartment,
                        accentColor = AccentOrange,
                        modifier = Modifier.weight(1f)
                    )

                    // Longest Streak
                    StatMetricCard(
                        title = "Longest Streak",
                        value = "${uiState.longestStreak} Days",
                        subtitle = "Personal record",
                        icon = Icons.Default.EmojiEvents,
                        accentColor = ActionPink,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // Chart Card (Weekly or Monthly)
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("analytics_chart_card")
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.timeFilter == AnalyticsTimeFilter.WEEKLY)
                                "Weekly Progress Chart" else "Monthly Progress Chart",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        // Highlight tip
                        Text(
                            text = "Tap bar for details",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    val activeChartList = if (uiState.timeFilter == AnalyticsTimeFilter.WEEKLY)
                        uiState.weeklyChart else uiState.monthlyChart

                    // Interactive Bar Chart
                    InteractiveProgressChart(
                        dataList = activeChartList,
                        onBarClick = { dayData ->
                            selectedDayData = dayData
                        }
                    )

                    // Tooltip display on click
                    selectedDayData?.let { data ->
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(PrimaryBlue.copy(alpha = 0.1f))
                                .border(1.dp, PrimaryBlue.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "${data.dayLabel}, ${data.dateString}",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${data.completedCount} of ${data.totalCount} habits completed",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(PrimaryBlue)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "${(data.percentage * 100).toInt()}%",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Section: Individual Habit Performance
        item {
            Column {
                Text(
                    text = "Habit Performance",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Completion rate and streaks by habit",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(uiState.habitPerformances, key = { it.habit.id }) { item ->
            HabitPerformanceCard(item = item)
        }

        // Section: Achievement Badges
        item {
            Column {
                Text(
                    text = "Achievement Badges",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Unlock rewards as you build lasting consistency",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        items(uiState.badges, key = { it.id }) { badge ->
            AchievementBadgeCard(badge = badge)
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun StatMetricCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(accentColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = subtitle,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun InteractiveProgressChart(
    dataList: List<DayChartData>,
    onBarClick: (DayChartData) -> Unit
) {
    if (dataList.isEmpty()) return

    val maxCount = maxOf(1, dataList.maxOf { it.totalCount })

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        dataList.forEach { dayData ->
            val animatedHeightFraction by animateFloatAsState(
                targetValue = if (maxCount > 0) (dayData.completedCount.toFloat() / maxCount.toFloat()).coerceIn(0.06f, 1f) else 0.06f,
                animationSpec = tween(durationMillis = 600),
                label = "bar_height"
            )

            val barColor = if (dayData.isToday) ActionPink else PrimaryBlue

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .padding(horizontal = 6.dp)
                    .clickable { onBarClick(dayData) }
            ) {
                // Completed count on top of bar
                Text(
                    text = "${dayData.completedCount}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (dayData.isToday) ActionPink else MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Bar
                Box(
                    modifier = Modifier
                        .width(if (dataList.size > 14) 14.dp else 26.dp)
                        .height((120 * animatedHeightFraction).dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(barColor)
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Day Label
                Text(
                    text = dayData.dayLabel,
                    fontSize = 11.sp,
                    fontWeight = if (dayData.isToday) FontWeight.Bold else FontWeight.Medium,
                    color = if (dayData.isToday) ActionPink else MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Day Number
                Text(
                    text = dayData.dayNumber,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun HabitPerformanceCard(item: HabitPerformanceUi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 1.5.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(item.parsedColor.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = HabitIconHelper.getIcon(item.habit.iconKey),
                        contentDescription = null,
                        tint = item.parsedColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.habit.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${item.habit.category}  •  ${item.completedCount} total completions",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${item.completionRatePercentage}%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = PrimaryBlue
                    )
                    if (item.currentStreak > 0) {
                        Text(
                            text = "🔥 ${item.currentStreak}d streak",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentOrange
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Linear Progress Bar
            LinearProgressIndicator(
                progress = { (item.completionRatePercentage.toFloat() / 100f).coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = item.parsedColor,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
fun AchievementBadgeCard(badge: AchievementBadge) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Badge Icon Circle
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(CircleShape)
                    .background(
                        if (badge.isUnlocked) HighlightYellow.copy(alpha = 0.35f)
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                    .border(
                        width = 1.5.dp,
                        color = if (badge.isUnlocked) AccentOrange else Color.Transparent,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (badge.isUnlocked) {
                    Icon(
                        imageVector = Icons.Default.MilitaryTech,
                        contentDescription = null,
                        tint = AccentOrange,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = badge.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (badge.isUnlocked) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(MintGreen.copy(alpha = 0.15f))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "UNLOCKED",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintGreen
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = badge.description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    LinearProgressIndicator(
                        progress = { badge.progress },
                        modifier = Modifier
                            .weight(1f)
                            .height(5.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (badge.isUnlocked) PrimaryBlue else AccentOrange,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = badge.progressText,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
