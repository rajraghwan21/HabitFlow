package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Habit
import com.example.ui.HabitFlowUiState
import com.example.ui.components.AddEditHabitDialog
import com.example.ui.components.CircularProgressCard
import com.example.ui.components.HabitCard
import com.example.ui.components.HabitFlowLogo
import com.example.ui.theme.ActionPink
import com.example.ui.theme.PrimaryBlue

@Composable
fun HomeScreen(
    uiState: HabitFlowUiState,
    onToggleHabit: (Long) -> Unit,
    onAddHabit: (title: String, target: String, category: String, iconKey: String, colorHex: String, reminderTime: String) -> Unit,
    onUpdateHabit: (Habit) -> Unit,
    onDeleteHabit: (Long) -> Unit,
    onProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddDialog by remember { mutableStateOf(false) }
    var habitToEdit by remember { mutableStateOf<Habit?>(null) }
    var habitToDelete by remember { mutableStateOf<Habit?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = PrimaryBlue,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .testTag("add_habit_fab")
                    .shadow(elevation = 6.dp, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add New Habit",
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .testTag("home_screen_content"),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Logo, Greeting, Date, Profile Icon
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        HabitFlowLogo()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = uiState.greeting,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Text(
                            text = uiState.formattedDate,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // User Profile Icon
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.12f))
                            .clickable(onClick = onProfileClick)
                            .testTag("user_profile_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        val initials = uiState.userSettings.userName
                            .split(" ")
                            .mapNotNull { it.firstOrNull()?.toString() }
                            .take(2)
                            .joinToString("")
                            .uppercase()

                        if (initials.isNotEmpty()) {
                            Text(
                                text = initials,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = PrimaryBlue
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = PrimaryBlue,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // Circular Progress Card
            item {
                CircularProgressCard(
                    completionPercentage = uiState.completionPercentage,
                    completionFraction = uiState.completionFraction,
                    completedCount = uiState.completedCount,
                    totalCount = uiState.totalCount,
                    currentStreak = uiState.currentStreak,
                    motivationalMessage = uiState.motivationalMessage
                )
            }

            // Section Header: "Today's Habits"
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FormatListBulleted,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Today's Habits",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // Count Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${uiState.completedCount}/${uiState.totalCount}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryBlue
                        )
                    }
                }
            }

            // Habits List
            if (uiState.habits.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.size(56.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "No habits created yet",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Tap the + button below to start your flow!",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            } else {
                items(uiState.habits, key = { it.habit.id }) { item ->
                    HabitCard(
                        habitItem = item,
                        onToggleCompletion = { onToggleHabit(item.habit.id) },
                        onEdit = { habitToEdit = item.habit },
                        onDelete = { habitToDelete = item.habit }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(60.dp)) // Padding for FAB and BottomNav
            }
        }
    }

    // Add New Habit Dialog
    if (showAddDialog) {
        AddEditHabitDialog(
            initialHabit = null,
            onDismiss = { showAddDialog = false },
            onSave = { title, target, category, iconKey, colorHex, reminderTime ->
                onAddHabit(title, target, category, iconKey, colorHex, reminderTime)
                showAddDialog = false
            }
        )
    }

    // Edit Habit Dialog
    habitToEdit?.let { habit ->
        AddEditHabitDialog(
            initialHabit = habit,
            onDismiss = { habitToEdit = null },
            onSave = { title, target, category, iconKey, colorHex, reminderTime ->
                onUpdateHabit(
                    habit.copy(
                        title = title,
                        target = target,
                        category = category,
                        iconKey = iconKey,
                        colorHex = colorHex,
                        reminderTime = reminderTime
                    )
                )
                habitToEdit = null
            }
        )
    }

    // Delete Confirmation Dialog
    habitToDelete?.let { habit ->
        AlertDialog(
            onDismissRequest = { habitToDelete = null },
            title = { Text("Delete Habit", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to delete \"${habit.title}\"? All its past completion logs will also be removed.") },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteHabit(habit.id)
                        habitToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = ActionPink),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("confirm_delete_button")
                ) {
                    Text("Delete", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { habitToDelete = null },
                    modifier = Modifier.testTag("cancel_delete_button")
                ) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.surface
        )
    }
}
