package com.mirea.todolistapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mirea.todolistapp.navigation.NavigationGraph
import com.mirea.todolistapp.presentation.viewmodel.TaskViewModel
import com.mirea.todolistapp.presentation.viewmodel.TaskViewModelFactory
import com.mirea.todolistapp.ui.theme.TodoListAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoListAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val application = application as TodoListApplication
                    val viewModel: TaskViewModel = viewModel(
                        factory = TaskViewModelFactory(
                            application.getTasksUseCase,
                            application.addTaskUseCase,
                            application.updateTaskUseCase,
                            application.deleteTaskUseCase,
                            application.importTasksUseCase,
                            application.preferencesRepository
                        )
                    )
                    NavigationGraph(viewModel = viewModel)
                }
            }
        }
    }
}