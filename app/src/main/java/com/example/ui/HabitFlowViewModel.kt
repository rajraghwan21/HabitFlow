package com.example.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.model.Habit
import com.example.data.model.HabitLog
import com.example.data.preferences.UserPreferencesManager
import com.example.data.preferences.UserSettings
import com.example.data.repository.HabitRepository
import com.example.ui.model.AchievementBadge
import com.example.ui.model.AnalyticsTimeFilter
import com.example.ui.model.DayChartData
import com.example.ui.model.HabitItemUiState
import com.example.ui.model.HabitPerformanceUi
import com.example.ui.model.NavigationTab
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class HabitFlowUiState(
    val currentTab: NavigationTab = NavigationTab.HOME,
    val greeting: String = "Good Morning!",
    val formattedDate: String = "",
    val todayDateString: String = "",
    val habits: List<HabitItemUiState> = emptyList(),
    val completedCount: Int = 0,
    val totalCount: Int = 0,
    val completionPercentage: Int = 0,
    val completionFraction: Float = 0f,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalCompletionsAllTime: Int = 0,
    val overallCompletionPercentage: Int = 0,
    val motivationalMessage: String = "Ready to conquer your day? Take the first step!",
    val timeFilter: AnalyticsTimeFilter = AnalyticsTimeFilter.WEEKLY,
    val weeklyChart: List<DayChartData> = emptyList(),
    val monthlyChart: List<DayChartData> = emptyList(),
    val habitPerformances: List<HabitPerformanceUi> = emptyList(),
    val badges: List<AchievementBadge> = emptyList(),
    val userSettings: UserSettings = UserSettings(),
    val isLoading: Boolean = false
)

class HabitFlowViewModel(
    private val repository: HabitRepository,
    private val preferencesManager: UserPreferencesManager
) : ViewModel() {

    private val _timeFilter = MutableStateFlow(AnalyticsTimeFilter.WEEKLY)
    private val _currentTab = MutableStateFlow(NavigationTab.HOME)

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val dayLabelFormat = SimpleDateFormat("EEE", Locale.getDefault())
    private val dayNumFormat = SimpleDateFormat("dd", Locale.getDefault())
    private val displayDateFormat = SimpleDateFormat("EEEE, MMM d", Locale.getDefault())

    val uiState: StateFlow<HabitFlowUiState> = combine(
        repository.allHabits,
        repository.allLogs,
        preferencesManager.settings,
        _timeFilter,
        _currentTab
    ) { habits, logs, settings, filter, tab ->
        buildUiState(habits, logs, settings, filter, tab)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HabitFlowUiState(isLoading = true)
    )

    init {
        viewModelScope.launch {
            val existing = repository.allHabits.firstOrNull()
            if (existing.isNullOrEmpty()) {
                repository.checkAndInitializeDefaults()
            }
        }
    }

    private fun buildUiState(
        habits: List<Habit>,
        logs: List<HabitLog>,
        settings: UserSettings,
        filter: AnalyticsTimeFilter,
        tab: NavigationTab
    ): HabitFlowUiState {
        val now = Date()
        val todayStr = sdf.format(now)
        val formattedDate = displayDateFormat.format(now)

        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val greeting = when (hour) {
            in 4..11 -> "Good Morning!"
            in 12..16 -> "Good Afternoon!"
            else -> "Good Evening!"
        }

        val todayLogs = logs.filter { it.dateString == todayStr }
        val completedHabitIdsToday = todayLogs.map { it.habitId }.toSet()

        // Logs grouped by habit
        val logsByHabit = logs.groupBy { it.habitId }

        // Habits UI list
        val habitItems = habits.map { habit ->
            val habitLogs = logsByHabit[habit.id] ?: emptyList()
            val streak = calculateHabitStreak(habit.id, habitLogs, todayStr)
            val parsedColor = parseHexColor(habit.colorHex)

            HabitItemUiState(
                habit = habit,
                isCompletedToday = completedHabitIdsToday.contains(habit.id),
                streak = streak,
                totalCompletions = habitLogs.size,
                parsedColor = parsedColor
            )
        }

        val totalHabits = habits.size
        val completedCount = habitItems.count { it.isCompletedToday }
        val percentage = if (totalHabits > 0) (completedCount * 100) / totalHabits else 0
        val fraction = if (totalHabits > 0) completedCount.toFloat() / totalHabits.toFloat() else 0f

        val motivational = when {
            percentage == 0 -> "Ready to conquer your day? Take the first step!"
            percentage in 1..49 -> "Great momentum! Keep the flow going."
            percentage in 50..99 -> "Almost there! Finish strong today."
            else -> "Outstanding! You completed all habits today! 🎉"
        }

        // Global streaks
        val allDatesWithCompletions = logs.map { it.dateString }.toSet()
        val currentStreak = calculateGlobalCurrentStreak(allDatesWithCompletions, todayStr)
        val longestStreak = calculateGlobalLongestStreak(allDatesWithCompletions)

        // Weekly chart (Last 7 days)
        val weeklyChart = generateChartData(7, logs, habits.size, todayStr)
        // Monthly chart (Last 30 days)
        val monthlyChart = generateChartData(30, logs, habits.size, todayStr)

        // Overall completion %
        val trackedDays = 7
        val expectedWeeklyCompletions = totalHabits * trackedDays
        val actualWeeklyCompletions = weeklyChart.sumOf { it.completedCount }
        val overallRate = if (expectedWeeklyCompletions > 0) {
            ((actualWeeklyCompletions * 100) / expectedWeeklyCompletions).coerceIn(0, 100)
        } else {
            0
        }

        // Individual Habit Performance
        val performances = habits.map { habit ->
            val hLogs = logsByHabit[habit.id] ?: emptyList()
            val totalDone = hLogs.size
            val rate = if (trackedDays > 0) {
                ((totalDone * 100) / trackedDays).coerceIn(0, 100)
            } else 0
            val streak = calculateHabitStreak(habit.id, hLogs, todayStr)

            HabitPerformanceUi(
                habit = habit,
                completedCount = totalDone,
                completionRatePercentage = rate,
                currentStreak = streak,
                parsedColor = parseHexColor(habit.colorHex)
            )
        }

        // Badges
        val badges = evaluateBadges(logs, habits, currentStreak, longestStreak)

        return HabitFlowUiState(
            currentTab = tab,
            greeting = greeting,
            formattedDate = formattedDate,
            todayDateString = todayStr,
            habits = habitItems,
            completedCount = completedCount,
            totalCount = totalHabits,
            completionPercentage = percentage,
            completionFraction = fraction,
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            totalCompletionsAllTime = logs.size,
            overallCompletionPercentage = overallRate,
            motivationalMessage = motivational,
            timeFilter = filter,
            weeklyChart = weeklyChart,
            monthlyChart = monthlyChart,
            habitPerformances = performances,
            badges = badges,
            userSettings = settings,
            isLoading = false
        )
    }

    private fun generateChartData(
        daysCount: Int,
        logs: List<HabitLog>,
        totalHabitsCount: Int,
        todayStr: String
    ): List<DayChartData> {
        val result = mutableListOf<DayChartData>()
        val cal = Calendar.getInstance()

        for (i in (daysCount - 1) downTo 0) {
            cal.time = Date()
            cal.add(Calendar.DAY_OF_YEAR, -i)
            val dStr = sdf.format(cal.time)
            val dLabel = dayLabelFormat.format(cal.time)
            val dNum = dayNumFormat.format(cal.time)

            val count = logs.count { it.dateString == dStr }
            val denom = if (totalHabitsCount > 0) totalHabitsCount else 1
            val pct = (count.toFloat() / denom.toFloat()).coerceIn(0f, 1f)

            result.add(
                DayChartData(
                    dateString = dStr,
                    dayLabel = dLabel,
                    dayNumber = dNum,
                    completedCount = count,
                    totalCount = totalHabitsCount,
                    percentage = pct,
                    isToday = (dStr == todayStr)
                )
            )
        }
        return result
    }

    private fun calculateHabitStreak(habitId: Long, logs: List<HabitLog>, todayStr: String): Int {
        if (logs.isEmpty()) return 0
        val dateSet = logs.map { it.dateString }.toSet()

        val cal = Calendar.getInstance()
        var streak = 0

        // Check if completed today; if not, check from yesterday
        var checkDate = if (dateSet.contains(todayStr)) {
            cal.time
        } else {
            cal.add(Calendar.DAY_OF_YEAR, -1)
            cal.time
        }

        while (true) {
            val dStr = sdf.format(checkDate)
            if (dateSet.contains(dStr)) {
                streak++
                cal.time = checkDate
                cal.add(Calendar.DAY_OF_YEAR, -1)
                checkDate = cal.time
            } else {
                break
            }
        }
        return streak
    }

    private fun calculateGlobalCurrentStreak(activeDates: Set<String>, todayStr: String): Int {
        if (activeDates.isEmpty()) return 0

        val cal = Calendar.getInstance()
        var streak = 0

        var checkDate = if (activeDates.contains(todayStr)) {
            cal.time
        } else {
            cal.add(Calendar.DAY_OF_YEAR, -1)
            cal.time
        }

        while (true) {
            val dStr = sdf.format(checkDate)
            if (activeDates.contains(dStr)) {
                streak++
                cal.time = checkDate
                cal.add(Calendar.DAY_OF_YEAR, -1)
                checkDate = cal.time
            } else {
                break
            }
        }
        return streak
    }

    private fun calculateGlobalLongestStreak(activeDates: Set<String>): Int {
        if (activeDates.isEmpty()) return 0
        val sortedDates = activeDates.toList().sorted()
        var maxStreak = 1
        var curStreak = 1

        val cal = Calendar.getInstance()
        for (i in 1 until sortedDates.size) {
            try {
                val prev = sdf.parse(sortedDates[i - 1])
                val curr = sdf.parse(sortedDates[i])
                if (prev != null && curr != null) {
                    cal.time = prev
                    cal.add(Calendar.DAY_OF_YEAR, 1)
                    if (sdf.format(cal.time) == sdf.format(curr)) {
                        curStreak++
                        if (curStreak > maxStreak) maxStreak = curStreak
                    } else if (sdf.format(prev) != sdf.format(curr)) {
                        curStreak = 1
                    }
                }
            } catch (e: Exception) {
                // ignore parsing exceptions
            }
        }
        return maxOf(maxStreak, curStreak)
    }

    private fun evaluateBadges(
        logs: List<HabitLog>,
        habits: List<Habit>,
        currentStreak: Int,
        longestStreak: Int
    ): List<AchievementBadge> {
        val totalCompletions = logs.size
        val bestStreak = maxOf(currentStreak, longestStreak)

        val waterHabitIds = habits.filter {
            it.title.contains("water", ignoreCase = true) || it.category.equals("health", ignoreCase = true)
        }.map { it.id }.toSet()
        val waterLogsCount = logs.count { waterHabitIds.contains(it.habitId) }

        val meditationHabitIds = habits.filter {
            it.title.contains("meditation", ignoreCase = true) || it.category.equals("mindfulness", ignoreCase = true)
        }.map { it.id }.toSet()
        val meditationLogsCount = logs.count { meditationHabitIds.contains(it.habitId) }

        return listOf(
            AchievementBadge(
                id = "first_step",
                title = "First Step",
                description = "Complete your very first habit",
                iconKey = "flag",
                isUnlocked = totalCompletions >= 1,
                progress = (totalCompletions.toFloat() / 1f).coerceIn(0f, 1f),
                progressText = if (totalCompletions >= 1) "Unlocked" else "$totalCompletions / 1"
            ),
            AchievementBadge(
                id = "three_day",
                title = "3-Day Flow",
                description = "Maintain a 3-day consecutive streak",
                iconKey = "bolt",
                isUnlocked = bestStreak >= 3,
                progress = (bestStreak.toFloat() / 3f).coerceIn(0f, 1f),
                progressText = if (bestStreak >= 3) "Unlocked" else "$bestStreak / 3 days"
            ),
            AchievementBadge(
                id = "consistency_champion",
                title = "Consistency Champion",
                description = "Achieve a solid 7-day habit streak",
                iconKey = "trophy",
                isUnlocked = bestStreak >= 7,
                progress = (bestStreak.toFloat() / 7f).coerceIn(0f, 1f),
                progressText = if (bestStreak >= 7) "Unlocked" else "$bestStreak / 7 days"
            ),
            AchievementBadge(
                id = "habit_master",
                title = "Habit Master",
                description = "Complete 25 habits in total",
                iconKey = "star",
                isUnlocked = totalCompletions >= 25,
                progress = (totalCompletions.toFloat() / 25f).coerceIn(0f, 1f),
                progressText = if (totalCompletions >= 25) "Unlocked" else "$totalCompletions / 25"
            ),
            AchievementBadge(
                id = "century_club",
                title = "Century Club",
                description = "Reach 50 total logged habits",
                iconKey = "military_tech",
                isUnlocked = totalCompletions >= 50,
                progress = (totalCompletions.toFloat() / 50f).coerceIn(0f, 1f),
                progressText = if (totalCompletions >= 50) "Unlocked" else "$totalCompletions / 50"
            ),
            AchievementBadge(
                id = "hydration_hero",
                title = "Hydration Hero",
                description = "Log drinking water 5 times",
                iconKey = "water_drop",
                isUnlocked = waterLogsCount >= 5,
                progress = (waterLogsCount.toFloat() / 5f).coerceIn(0f, 1f),
                progressText = if (waterLogsCount >= 5) "Unlocked" else "$waterLogsCount / 5"
            ),
            AchievementBadge(
                id = "mindful_zen",
                title = "Mindful Zen",
                description = "Log meditation or mindfulness 5 times",
                iconKey = "spa",
                isUnlocked = meditationLogsCount >= 5,
                progress = (meditationLogsCount.toFloat() / 5f).coerceIn(0f, 1f),
                progressText = if (meditationLogsCount >= 5) "Unlocked" else "$meditationLogsCount / 5"
            )
        )
    }

    private fun parseHexColor(hex: String): Color {
        return try {
            val clean = hex.removePrefix("#")
            val colorLong = clean.toLong(16)
            if (clean.length == 6) {
                Color(0xFF000000 or colorLong)
            } else {
                Color(colorLong)
            }
        } catch (e: Exception) {
            Color(0xFF2391FF)
        }
    }

    // Actions
    fun setTab(tab: NavigationTab) {
        _currentTab.value = tab
    }

    fun setTimeFilter(filter: AnalyticsTimeFilter) {
        _timeFilter.value = filter
    }

    fun toggleHabit(habitId: Long) {
        viewModelScope.launch {
            val todayStr = sdf.format(Date())
            val currentItem = uiState.value.habits.find { it.habit.id == habitId }
            val isDone = currentItem?.isCompletedToday ?: false
            repository.toggleCompletion(habitId, todayStr, isDone)
        }
    }

    fun addNewHabit(
        title: String,
        target: String,
        category: String,
        iconKey: String,
        colorHex: String,
        reminderTime: String
    ) {
        viewModelScope.launch {
            val habit = Habit(
                title = title.trim(),
                target = target.trim(),
                category = category.trim(),
                iconKey = iconKey,
                colorHex = colorHex,
                reminderTime = reminderTime.trim()
            )
            repository.addHabit(habit)
        }
    }

    fun updateHabit(habit: Habit) {
        viewModelScope.launch {
            repository.updateHabit(habit)
        }
    }

    fun deleteHabit(habitId: Long) {
        viewModelScope.launch {
            repository.deleteHabit(habitId)
        }
    }

    fun updateProfile(name: String, email: String) {
        preferencesManager.updateProfile(name, email)
    }

    fun setThemeMode(mode: String) {
        preferencesManager.setThemeMode(mode)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        preferencesManager.setNotificationsEnabled(enabled)
    }

    fun setReminderTime(time: String) {
        preferencesManager.setReminderTime(time)
    }

    fun setAccentColor(color: String) {
        preferencesManager.setAccentColor(color)
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
            preferencesManager.resetSettings()
        }
    }

    fun resetToDefaults() {
        viewModelScope.launch {
            repository.checkAndInitializeDefaults()
        }
    }

    fun exportDataAsJson(): String {
        val habits = uiState.value.habits.map { it.habit }
        val sb = StringBuilder()
        sb.append("{\n")
        sb.append("  \"app\": \"HabitFlow\",\n")
        sb.append("  \"exportDate\": \"${sdf.format(Date())}\",\n")
        sb.append("  \"habitsCount\": ${habits.size},\n")
        sb.append("  \"totalCompletions\": ${uiState.value.totalCompletionsAllTime},\n")
        sb.append("  \"currentStreak\": ${uiState.value.currentStreak},\n")
        sb.append("  \"habits\": [\n")
        habits.forEachIndexed { index, h ->
            sb.append("    {\n")
            sb.append("      \"id\": ${h.id},\n")
            sb.append("      \"title\": \"${h.title.replace("\"", "\\\"")}\",\n")
            sb.append("      \"target\": \"${h.target.replace("\"", "\\\"")}\",\n")
            sb.append("      \"category\": \"${h.category}\",\n")
            sb.append("      \"reminderTime\": \"${h.reminderTime}\",\n")
            sb.append("      \"color\": \"${h.colorHex}\"\n")
            sb.append("    }${if (index < habits.size - 1) "," else ""}\n")
        }
        sb.append("  ]\n")
        sb.append("}")
        return sb.toString()
    }
}

class HabitFlowViewModelFactory(
    private val repository: HabitRepository,
    private val preferencesManager: UserPreferencesManager
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HabitFlowViewModel::class.java)) {
            return HabitFlowViewModel(repository, preferencesManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
