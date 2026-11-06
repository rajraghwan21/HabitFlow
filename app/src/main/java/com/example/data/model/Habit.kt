package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class Habit(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val target: String,
    val category: String,
    val iconKey: String = "check",
    val colorHex: String = "#2391FF",
    val reminderTime: String = "08:00 AM",
    val createdAt: Long = System.currentTimeMillis(),
    val isArchived: Boolean = false
)

@Entity(tableName = "habit_logs")
data class HabitLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val habitId: Long,
    val dateString: String, // format YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis()
)
