package com.example.todolist.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mirea.todolistapp.presentation.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTaskScreen(
    taskId: String?,
    viewModel: TaskViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    val isNewTask = taskId == "new"
    val existingTask = if (!isNewTask) {
        taskId?.toIntOrNull()?.let { id ->
            uiState.tasks.find { it.id == id }
        }
    } else null

    var title by remember { mutableStateOf(existingTask?.title ?: "") }
    var description by remember { mutableStateOf(existingTask?.description ?: "") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isNewTask) "Новая задача" else "Редактировать") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (isNewTask) {
                                    viewModel.addTask(title, description)
                                } else {
                                    existingTask?.let { task ->
                                        viewModel.updateTask(
                                            task.copy(
                                                title = title,
                                                description = description
                                            )
                                        )
                                    }
                                }
                                onBackClick()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Save, contentDescription = "Сохранить")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Название задачи") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Описание (опционально)") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
            )
        }
    }
}