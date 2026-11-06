package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Habit
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.ActionPink
import com.example.ui.theme.MintGreen
import com.example.ui.theme.PrimaryBlue
import com.example.ui.theme.PurpleLavender

@Composable
fun AddEditHabitDialog(
    initialHabit: Habit? = null,
    onDismiss: () -> Unit,
    onSave: (title: String, target: String, category: String, iconKey: String, colorHex: String, reminderTime: String) -> Unit
) {
    var title by remember { mutableStateOf(initialHabit?.title ?: "") }
    var target by remember { mutableStateOf(initialHabit?.target ?: "") }
    var selectedCategory by remember { mutableStateOf(initialHabit?.category ?: "Health") }
    var selectedIcon by remember { mutableStateOf(initialHabit?.iconKey ?: "check") }
    var selectedColor by remember { mutableStateOf(initialHabit?.colorHex ?: "#2391FF") }
    var reminderTime by remember { mutableStateOf(initialHabit?.reminderTime ?: "08:00 AM") }
    var titleError by remember { mutableStateOf(false) }

    val categories = listOf("Learning", "Fitness", "Health", "Productivity", "Mindfulness")
    val colors = listOf("#2391FF", "#F70063", "#FFCD54", "#10B981", "#8B5CF6")
    val icons = listOf("book", "fitness", "water", "code", "meditation", "star")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (initialHabit == null) "Create New Habit" else "Edit Habit",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                // Habit Name
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        if (it.isNotBlank()) titleError = false
                    },
                    label = { Text("Habit Name") },
                    placeholder = { Text("e.g., Reading, Running, Drink Water") },
                    isError = titleError,
                    supportingText = if (titleError) {
                        { Text("Habit name cannot be empty") }
                    } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("habit_title_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Target
                OutlinedTextField(
                    value = target,
                    onValueChange = { target = it },
                    label = { Text("Target / Goal") },
                    placeholder = { Text("e.g., 30 minutes, 8 glasses, 2 hours") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("habit_target_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Category selector
                Text(
                    text = "Category",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categories.forEach { cat ->
                        val isSelected = selectedCategory == cat
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedCategory = cat
                                // Auto-assign a suitable icon if default
                                when (cat) {
                                    "Learning" -> selectedIcon = "book"
                                    "Fitness" -> selectedIcon = "fitness"
                                    "Health" -> selectedIcon = "water"
                                    "Productivity" -> selectedIcon = "code"
                                    "Mindfulness" -> selectedIcon = "meditation"
                                }
                            },
                            label = { Text(cat, fontSize = 12.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryBlue,
                                selectedLabelColor = Color.White
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Icon selection
                Text(
                    text = "Icon",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    icons.forEach { iconKey ->
                        val isSelected = selectedIcon == iconKey
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isSelected) PrimaryBlue.copy(alpha = 0.2f)
                                    else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) PrimaryBlue else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedIcon = iconKey },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = HabitIconHelper.getIcon(iconKey),
                                contentDescription = iconKey,
                                tint = if (isSelected) PrimaryBlue else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Color selection
                Text(
                    text = "Theme Color",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    colors.forEach { hex ->
                        val color = try {
                            Color(0xFF000000 or hex.removePrefix("#").toLong(16))
                        } catch (e: Exception) {
                            PrimaryBlue
                        }
                        val isSelected = selectedColor.equals(hex, ignoreCase = true)
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { selectedColor = hex },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Reminder Time
                OutlinedTextField(
                    value = reminderTime,
                    onValueChange = { reminderTime = it },
                    label = { Text("Daily Reminder Time") },
                    placeholder = { Text("e.g., 08:00 AM, 09:30 PM") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("habit_reminder_input")
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        titleError = true
                        return@Button
                    }
                    val tgt = if (target.isBlank()) "Daily Goal" else target
                    onSave(title, tgt, selectedCategory, selectedIcon, selectedColor, reminderTime)
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("habit_save_button")
            ) {
                Text("Save Habit", fontWeight = FontWeight.Bold, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("habit_cancel_button")
            ) {
                Text("Cancel", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}
