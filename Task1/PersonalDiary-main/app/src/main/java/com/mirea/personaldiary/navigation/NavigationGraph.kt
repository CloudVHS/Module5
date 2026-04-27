package com.mirea.personaldiary.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import com.mirea.personaldiary.presentation.screen.DiaryScreen
import com.mirea.personaldiary.presentation.screen.EntryScreen
import com.mirea.personaldiary.presentation.viewmodel.DiaryViewModel

sealed class Screen(val route: String) {
    object List : Screen("list")
    object Entry : Screen("entry/{fileName}") {
        fun createRoute(fileName: String): String = "entry/$fileName"
        fun createNewRoute(): String = "entry/new"
    }
}

fun NavGraphBuilder.diaryNavigationGraph(
    navController: NavHostController,
    viewModel: DiaryViewModel
) {
    composable(Screen.List.route) {
        DiaryScreen(
            viewModel = viewModel,
            onEntryClick = { entry ->
                navController.navigate(Screen.Entry.createRoute(entry.fileName))
            },
            onAddClick = {
                navController.navigate(Screen.Entry.createNewRoute())
            }
        )
    }

    composable(
        route = Screen.Entry.route,
        arguments = listOf(navArgument("fileName") {
            type = NavType.StringType
            defaultValue = "new"
        })
    ) { backStackEntry ->
        val fileName = backStackEntry.arguments?.getString("fileName")
        EntryScreen(
            viewModel = viewModel,
            fileName = fileName,
            onBackClick = { navController.popBackStack() }
        )
    }
}