package com.mirea.todolistapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.presentation.ui.screen.AddEditTaskScreen
import com.example.todolist.presentation.ui.screen.TaskListScreen
import com.mirea.todolistapp.presentation.viewmodel.TaskViewModel

@Composable
fun NavigationGraph(
    viewModel: TaskViewModel
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "task_list"
    ) {
        composable("task_list") {
            TaskListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("add_edit_task/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")
            AddEditTaskScreen(
                taskId = taskId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}