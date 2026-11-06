package com.example.data.preferences

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserSettings(
    val userName: String = "Alex Morgan",
    val userEmail: String = "alex.morgan@habitflow.app",
    val themeMode: String = "light", // "light", "dark", "system"
    val notificationsEnabled: Boolean = true,
    val reminderTime: String = "08:00 AM",
    val accentColor: String = "#2391FF"
)

class UserPreferencesManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("habitflow_prefs", Context.MODE_PRIVATE)

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<UserSettings> = _settings.asStateFlow()

    private fun loadSettings(): UserSettings {
        return UserSettings(
            userName = prefs.getString("user_name", "Alex Morgan") ?: "Alex Morgan",
            userEmail = prefs.getString("user_email", "alex.morgan@habitflow.app") ?: "alex.morgan@habitflow.app",
            themeMode = prefs.getString("theme_mode", "light") ?: "light",
            notificationsEnabled = prefs.getBoolean("notifications_enabled", true),
            reminderTime = prefs.getString("reminder_time", "08:00 AM") ?: "08:00 AM",
            accentColor = prefs.getString("accent_color", "#2391FF") ?: "#2391FF"
        )
    }

    fun updateProfile(name: String, email: String) {
        prefs.edit()
            .putString("user_name", name)
            .putString("user_email", email)
            .apply()
        _settings.value = _settings.value.copy(userName = name, userEmail = email)
    }

    fun setThemeMode(mode: String) {
        prefs.edit().putString("theme_mode", mode).apply()
        _settings.value = _settings.value.copy(themeMode = mode)
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("notifications_enabled", enabled).apply()
        _settings.value = _settings.value.copy(notificationsEnabled = enabled)
    }

    fun setReminderTime(time: String) {
        prefs.edit().putString("reminder_time", time).apply()
        _settings.value = _settings.value.copy(reminderTime = time)
    }

    fun setAccentColor(color: String) {
        prefs.edit().putString("accent_color", color).apply()
        _settings.value = _settings.value.copy(accentColor = color)
    }

    fun resetSettings() {
        prefs.edit().clear().apply()
        _settings.value = UserSettings()
    }
}
