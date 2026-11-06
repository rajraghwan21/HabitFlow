package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.preferences.UserSettings
import com.example.ui.components.HabitFlowLogo
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.ActionPink
import com.example.ui.theme.PrimaryBlue

@Composable
fun SettingsScreen(
    userSettings: UserSettings,
    onUpdateProfile: (name: String, email: String) -> Unit,
    onThemeModeChange: (String) -> Unit,
    onNotificationsToggle: (Boolean) -> Unit,
    onReminderTimeChange: (String) -> Unit,
    onAccentColorChange: (String) -> Unit,
    onExportData: () -> String,
    onClearData: () -> Unit,
    onResetDefaults: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var showProfileDialog by remember { mutableStateOf(false) }
    var showReminderDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showExportDialog by remember { mutableStateOf(false) }
    var showClearDataDialog by remember { mutableStateOf(false) }
    var showResetDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("settings_screen"),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Screen Header
        item {
            Column {
                Text(
                    text = "Settings & Preferences",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Manage profile, appearance, reminders & data",
                    fontSize = 13.5.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Profile Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("profile_card")
                    .shadow(elevation = 2.dp, shape = RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        val initials = userSettings.userName
                            .split(" ")
                            .mapNotNull { it.firstOrNull()?.toString() }
                            .take(2)
                            .joinToString("")
                            .uppercase()

                        if (initials.isNotEmpty()) {
                            Text(
                                text = initials,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = PrimaryBlue
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = userSettings.userName,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = userSettings.userEmail,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(PrimaryBlue.copy(alpha = 0.1f))
                            .clickable { showProfileDialog = true }
                            .padding(8.dp)
                            .testTag("edit_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Section: Appearance
        item {
            SettingsSectionHeader(title = "Appearance & Theme")
        }

        item {
            SettingsGroupCard {
                // Theme Mode (Light / Dark / System)
                SettingsRowItem(
                    icon = Icons.Default.Brightness4,
                    iconTint = PrimaryBlue,
                    title = "App Theme Mode",
                    subtitle = when (userSettings.themeMode) {
                        "dark" -> "Dark Mode"
                        "light" -> "Light Mode"
                        else -> "System Default"
                    },
                    onClick = { showThemeDialog = true },
                    testTag = "theme_mode_item"
                )

                // Theme Accent Color Preview
                SettingsRowItem(
                    icon = Icons.Default.Palette,
                    iconTint = ActionPink,
                    title = "Accent Theme",
                    subtitle = "HabitFlow Signature Palette",
                    onClick = {
                        Toast.makeText(context, "Using HabitFlow Signature colors", Toast.LENGTH_SHORT).show()
                    },
                    trailingContent = {
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(PrimaryBlue))
                            Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(ActionPink))
                            Box(modifier = Modifier.size(16.dp).clip(CircleShape).background(AccentOrange))
                        }
                    }
                )
            }
        }

        // Section: Reminders & Notifications
        item {
            SettingsSectionHeader(title = "Reminders & Alerts")
        }

        item {
            SettingsGroupCard {
                // Daily Reminders Toggle
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(PrimaryBlue.copy(alpha = 0.12f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = PrimaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Daily Habit Reminders",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Stay on track every day",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Switch(
                        checked = userSettings.notificationsEnabled,
                        onCheckedChange = { onNotificationsToggle(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = PrimaryBlue
                        ),
                        modifier = Modifier.testTag("notification_switch")
                    )
                }

                // Reminder Time
                SettingsRowItem(
                    icon = Icons.Default.Schedule,
                    iconTint = AccentOrange,
                    title = "Reminder Time",
                    subtitle = userSettings.reminderTime,
                    onClick = { showReminderDialog = true },
                    testTag = "reminder_time_item"
                )
            }
        }

        // Section: Data & Backup
        item {
            SettingsSectionHeader(title = "Data Management")
        }

        item {
            SettingsGroupCard {
                // Export Data
                SettingsRowItem(
                    icon = Icons.Default.FileDownload,
                    iconTint = PrimaryBlue,
                    title = "Export Habit Data",
                    subtitle = "Export habits and logs as JSON",
                    onClick = { showExportDialog = true },
                    testTag = "export_data_item"
                )

                // Reset to Sample Habits
                SettingsRowItem(
                    icon = Icons.Default.RestartAlt,
                    iconTint = AccentOrange,
                    title = "Reset to Sample Habits",
                    subtitle = "Restore 5 example habits & history",
                    onClick = { showResetDialog = true },
                    testTag = "reset_defaults_item"
                )

                // Clear All Data
                SettingsRowItem(
                    icon = Icons.Default.DeleteForever,
                    iconTint = ActionPink,
                    title = "Clear Local Data",
                    subtitle = "Delete all habits and history",
                    onClick = { showClearDataDialog = true },
                    testTag = "clear_data_item"
                )
            }
        }

        // About Application Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("about_card")
                    .shadow(elevation = 1.dp, shape = RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HabitFlowLogo(iconSize = 42)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Version 1.0.0",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "“Small daily improvements over time lead to stunning results.”",
                        fontSize = 12.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }

    // Profile Edit Dialog
    if (showProfileDialog) {
        var tempName by remember { mutableStateOf(userSettings.userName) }
        var tempEmail by remember { mutableStateOf(userSettings.userEmail) }

        AlertDialog(
            onDismissRequest = { showProfileDialog = false },
            title = { Text("Edit Profile", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = tempName,
                        onValueChange = { tempName = it },
                        label = { Text("Your Name") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("profile_name_input")
                    )
                    OutlinedTextField(
                        value = tempEmail,
                        onValueChange = { tempEmail = it },
                        label = { Text("Your Email") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("profile_email_input")
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onUpdateProfile(tempName.ifBlank { "Alex Morgan" }, tempEmail.ifBlank { "alex.morgan@habitflow.app" })
                        showProfileDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showProfileDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // Theme Mode Selection Dialog
    if (showThemeDialog) {
        val themes = listOf(
            "light" to "Light Mode",
            "dark" to "Dark Mode",
            "system" to "System Default"
        )

        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Choose Theme Mode", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    themes.forEach { (mode, label) ->
                        val isSelected = userSettings.themeMode == mode
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) PrimaryBlue.copy(alpha = 0.12f)
                                    else Color.Transparent
                                )
                                .clickable {
                                    onThemeModeChange(mode)
                                    showThemeDialog = false
                                }
                                .padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = label,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.onSurface
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = PrimaryBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Done")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // Reminder Time Dialog
    if (showReminderDialog) {
        var tempTime by remember { mutableStateOf(userSettings.reminderTime) }
        val presetTimes = listOf("07:00 AM", "08:00 AM", "09:00 AM", "12:00 PM", "08:00 PM", "09:30 PM")

        AlertDialog(
            onDismissRequest = { showReminderDialog = false },
            title = { Text("Daily Reminder Time", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = tempTime,
                        onValueChange = { tempTime = it },
                        label = { Text("Custom Time") },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().testTag("reminder_time_input")
                    )

                    Text(
                        text = "Or choose a preset:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        presetTimes.take(3).forEach { time ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable { tempTime = time }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(time, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        onReminderTimeChange(tempTime.ifBlank { "08:00 AM" })
                        showReminderDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showReminderDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // Export Data Dialog
    if (showExportDialog) {
        val jsonData = onExportData()

        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Export Habit Data", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text(
                        text = "Here is your habit data in JSON format:",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = jsonData,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("HabitFlow Data", jsonData)
                        clipboard.setPrimaryClip(clip)
                        Toast.makeText(context, "Data copied to clipboard!", Toast.LENGTH_SHORT).show()
                        showExportDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("copy_json_button")
                ) {
                    Icon(imageVector = Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Copy JSON", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Close")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // Reset to defaults dialog
    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Reset to Sample Habits?", fontWeight = FontWeight.Bold) },
            text = { Text("This will reload the 5 example habits (Reading, Exercise, Water, Coding, Meditation) with sample streak data.") },
            confirmButton = {
                Button(
                    onClick = {
                        onResetDefaults()
                        showResetDialog = false
                        Toast.makeText(context, "Habits restored!", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Reset", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }

    // Clear All Data Dialog
    if (showClearDataDialog) {
        AlertDialog(
            onDismissRequest = { showClearDataDialog = false },
            title = { Text("Clear All Data?", fontWeight = FontWeight.Bold, color = ActionPink) },
            text = { Text("Are you sure you want to permanently clear all habits and completion history? This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        onClearData()
                        showClearDataDialog = false
                        Toast.makeText(context, "All data cleared", Toast.LENGTH_SHORT).show()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ActionPink),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("confirm_clear_data_button")
                ) {
                    Text("Clear Everything", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDataDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Composable
fun SettingsSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, top = 6.dp)
    )
}

@Composable
fun SettingsGroupCard(content: @Composable () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(elevation = 1.5.dp, shape = RoundedCornerShape(18.dp)),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            content()
        }
    }
}

@Composable
fun SettingsRowItem(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingContent: (@Composable () -> Unit)? = null,
    testTag: String = ""
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .then(if (testTag.isNotEmpty()) Modifier.testTag(testTag) else Modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(iconTint.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (trailingContent != null) {
            trailingContent()
        } else {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
