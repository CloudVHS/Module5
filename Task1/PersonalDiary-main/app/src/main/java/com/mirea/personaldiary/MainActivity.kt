package com.mirea.personaldiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.mirea.personaldiary.navigation.diaryNavigationGraph
import com.mirea.personaldiary.presentation.viewmodel.DiaryViewModel
import com.mirea.personaldiary.presentation.DiaryViewModelFactory
import com.mirea.personaldiary.ui.theme.PersonalDiaryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PersonalDiaryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: DiaryViewModel = viewModel(
                        factory = DiaryViewModelFactory(this)
                    )
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "list"
                    ) {
                        diaryNavigationGraph(navController, viewModel)
                    }
                }
            }
        }
    }
}