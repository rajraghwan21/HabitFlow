package com.example.data.repository

import com.example.data.db.HabitDao
import com.example.data.model.Habit
import com.example.data.model.HabitLog
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class HabitRepository(private val habitDao: HabitDao) {

    val allHabits: Flow<List<Habit>> = habitDao.getAllHabits()
    val allLogs: Flow<List<HabitLog>> = habitDao.getAllLogs()

    suspend fun checkAndInitializeDefaults() {
        val initialList = listOf(
            Habit(
                title = "Reading",
                target = "30 minutes",
                category = "Learning",
                iconKey = "book",
                colorHex = "#2391FF",
                reminderTime = "08:30 PM"
            ),
            Habit(
                title = "Exercise",
                target = "45 minutes",
                category = "Fitness",
                iconKey = "fitness",
                colorHex = "#F70063",
                reminderTime = "07:00 AM"
            ),
            Habit(
                title = "Drinking Water",
                target = "8 glasses",
                category = "Health",
                iconKey = "water",
                colorHex = "#2391FF",
                reminderTime = "Every 2 hours"
            ),
            Habit(
                title = "Coding",
                target = "2 hours",
                category = "Productivity",
                iconKey = "code",
                colorHex = "#FFCD54",
                reminderTime = "10:00 AM"
            ),
            Habit(
                title = "Meditation",
                target = "15 minutes",
                category = "Mindfulness",
                iconKey = "meditation",
                colorHex = "#10B981",
                reminderTime = "09:00 PM"
            )
        )

        habitDao.clearAllHabits()
        habitDao.clearAllLogs()

        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val cal = Calendar.getInstance()

        val sampleLogs = mutableListOf<HabitLog>()

        // Insert habits one by one to get their generated IDs
        for (i in initialList.indices) {
            val habit = initialList[i]
            val habitId = habitDao.insertHabit(habit)

            // Seed historical completions for the last 6 days to show rich charts
            for (dayOffset in 6 downTo 1) {
                cal.time = Date()
                cal.add(Calendar.DAY_OF_YEAR, -dayOffset)
                val pastDate = sdf.format(cal.time)

                // Give realistic distribution (e.g., water and reading frequently done)
                val shouldComplete = when (i) {
                    0 -> dayOffset % 2 == 0 || dayOffset == 1 // Reading
                    1 -> dayOffset in listOf(1, 2, 4, 6) // Exercise
                    2 -> true // Water every day
                    3 -> dayOffset in listOf(1, 2, 3, 5) // Coding
                    4 -> dayOffset in listOf(1, 2, 3, 4) // Meditation
                    else -> true
                }

                if (shouldComplete) {
                    sampleLogs.add(
                        HabitLog(
                            habitId = habitId,
                            dateString = pastDate,
                            timestamp = cal.timeInMillis
                        )
                    )
                }
            }

            // Also complete 2 habits for today to start with progress
            if (i == 0 || i == 2) {
                val todayStr = sdf.format(Date())
                sampleLogs.add(
                    HabitLog(
                        habitId = habitId,
                        dateString = todayStr,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
        }

        habitDao.insertAllLogs(sampleLogs)
    }

    suspend fun addHabit(habit: Habit): Long {
        return habitDao.insertHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habitId: Long) {
        habitDao.deleteHabitById(habitId)
        habitDao.deleteLogsForHabit(habitId)
    }

    suspend fun toggleCompletion(habitId: Long, dateString: String, isCurrentlyCompleted: Boolean) {
        if (isCurrentlyCompleted) {
            habitDao.deleteLog(habitId, dateString)
        } else {
            habitDao.insertLog(
                HabitLog(
                    habitId = habitId,
                    dateString = dateString,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }

    suspend fun clearAllData() {
        habitDao.clearAllHabits()
        habitDao.clearAllLogs()
    }
}
