package com.example.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.MilitaryTech
import androidx.compose.material.icons.outlined.Spa
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.vector.ImageVector

object HabitIconHelper {
    fun getIcon(key: String): ImageVector {
        return when (key.lowercase()) {
            "book", "reading" -> Icons.Default.MenuBook
            "fitness", "exercise", "workout" -> Icons.Default.FitnessCenter
            "water", "drinking water", "water_drop" -> Icons.Default.WaterDrop
            "code", "coding", "work" -> Icons.Default.Code
            "meditation", "mindfulness", "zen", "spa" -> Icons.Default.SelfImprovement
            "star" -> Icons.Default.Star
            "fire", "flame", "streak" -> Icons.Default.LocalFireDepartment
            "flag" -> Icons.Outlined.Flag
            "trophy", "military_tech" -> Icons.Outlined.MilitaryTech
            else -> Icons.Default.AutoAwesome
        }
    }
}
