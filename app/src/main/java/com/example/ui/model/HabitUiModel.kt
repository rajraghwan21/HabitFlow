package com.example.ui.model

import androidx.compose.ui.graphics.Color
import com.example.data.model.Habit

data class HabitItemUiState(
    val habit: Habit,
    val isCompletedToday: Boolean,
    val streak: Int,
    val totalCompletions: Int,
    val parsedColor: Color
)

data class DayChartData(
    val dateString: String,
    val dayLabel: String,       // e.g. "Mon", "Tue"
    val dayNumber: String,      // e.g. "06"
    val completedCount: Int,
    val totalCount: Int,
    val percentage: Float,      // 0.0 to 1.0
    val isToday: Boolean
)

data class HabitPerformanceUi(
    val habit: Habit,
    val completedCount: Int,
    val completionRatePercentage: Int, // 0-100%
    val currentStreak: Int,
    val parsedColor: Color
)

data class AchievementBadge(
    val id: String,
    val title: String,
    val description: String,
    val iconKey: String,
    val isUnlocked: Boolean,
    val progress: Float, // 0.0 to 1.0
    val progressText: String
)

enum class AnalyticsTimeFilter {
    WEEKLY,
    MONTHLY
}

enum class NavigationTab {
    HOME,
    ANALYTICS,
    SETTINGS
}
