package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.HabitFlowBottomNav
import com.example.ui.model.NavigationTab
import com.example.ui.screens.AnalyticsScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.HabitFlowTheme

@Composable
fun HabitFlowApp(
    viewModel: HabitFlowViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isDarkTheme = when (uiState.userSettings.themeMode) {
        "dark" -> true
        "light" -> false
        else -> isSystemInDarkTheme()
    }

    HabitFlowTheme(darkTheme = isDarkTheme) {
        // Back navigation handler: return to Home tab if on another screen
        BackHandler(enabled = uiState.currentTab != NavigationTab.HOME) {
            viewModel.setTab(NavigationTab.HOME)
        }

        Scaffold(
            bottomBar = {
                HabitFlowBottomNav(
                    currentTab = uiState.currentTab,
                    onTabSelected = { tab -> viewModel.setTab(tab) }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                AnimatedContent(
                    targetState = uiState.currentTab,
                    transitionSpec = {
                        fadeIn(animationSpec = tween(220)) togetherWith
                                fadeOut(animationSpec = tween(180))
                    },
                    label = "tab_transition"
                ) { tab ->
                    when (tab) {
                        NavigationTab.HOME -> {
                            HomeScreen(
                                uiState = uiState,
                                onToggleHabit = { habitId -> viewModel.toggleHabit(habitId) },
                                onAddHabit = { title, target, category, iconKey, colorHex, reminderTime ->
                                    viewModel.addNewHabit(title, target, category, iconKey, colorHex, reminderTime)
                                },
                                onUpdateHabit = { habit -> viewModel.updateHabit(habit) },
                                onDeleteHabit = { habitId -> viewModel.deleteHabit(habitId) },
                                onProfileClick = { viewModel.setTab(NavigationTab.SETTINGS) }
                            )
                        }

                        NavigationTab.ANALYTICS -> {
                            AnalyticsScreen(
                                uiState = uiState,
                                onFilterChange = { filter -> viewModel.setTimeFilter(filter) }
                            )
                        }

                        NavigationTab.SETTINGS -> {
                            SettingsScreen(
                                userSettings = uiState.userSettings,
                                onUpdateProfile = { name, email -> viewModel.updateProfile(name, email) },
                                onThemeModeChange = { mode -> viewModel.setThemeMode(mode) },
                                onNotificationsToggle = { enabled -> viewModel.setNotificationsEnabled(enabled) },
                                onReminderTimeChange = { time -> viewModel.setReminderTime(time) },
                                onAccentColorChange = { color -> viewModel.setAccentColor(color) },
                                onExportData = { viewModel.exportDataAsJson() },
                                onClearData = { viewModel.clearAllData() },
                                onResetDefaults = { viewModel.resetToDefaults() }
                            )
                        }
                    }
                }
            }
        }
    }
}
