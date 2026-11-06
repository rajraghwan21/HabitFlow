package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.data.db.AppDatabase
import com.example.data.preferences.UserPreferencesManager
import com.example.data.repository.HabitRepository
import com.example.ui.HabitFlowApp
import com.example.ui.HabitFlowViewModel
import com.example.ui.HabitFlowViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: HabitFlowViewModel by viewModels {
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = HabitRepository(database.habitDao())
        val preferencesManager = UserPreferencesManager(applicationContext)
        HabitFlowViewModelFactory(repository, preferencesManager)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HabitFlowApp(viewModel = viewModel)
        }
    }
}
