package com.mirea.todolistapp.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirea.todolistapp.data.preferences.PreferencesRepository
import com.mirea.todolistapp.domain.model.Task
import com.mirea.todolistapp.domain.usecase.AddTaskUseCase
import com.mirea.todolistapp.domain.usecase.DeleteTaskUseCase
import com.mirea.todolistapp.domain.usecase.GetTasksUseCase
import com.mirea.todolistapp.domain.usecase.ImportTasksUseCase
import com.mirea.todolistapp.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class TaskUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val isCompletedColorEnabled: Boolean = false,
    val snackbarMessage: String? = null
)

class TaskViewModel(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val importTasksUseCase: ImportTasksUseCase,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
        loadColorPreferences()
    }

    private fun loadTasks() {
        getTasksUseCase()
            .onEach { tasks ->
                _uiState.update { it.copy(tasks = tasks, isLoading = false) }
            }
            .launchIn(viewModelScope)
    }

    private fun loadColorPreferences() {
        preferencesRepository.isCompletedTaskColorEnabled
            .onEach { enabled ->
                _uiState.update { it.copy(isCompletedColorEnabled = enabled) }
            }
            .launchIn(viewModelScope)
    }

    fun addTask(title: String, description: String) {
        if (title.isBlank()) {
            _uiState.update { it.copy(snackbarMessage = "Введите название задачи") }
            return
        }

        viewModelScope.launch {
            val task = Task(
                title = title,
                description = description
            )
            addTaskUseCase(task)
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task)
        }
    }

    fun toggleTaskCompletion(task: Task) {
        val updatedTask = task.copy(isCompleted = !task.isCompleted)
        updateTask(updatedTask)
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task)
            _uiState.update { it.copy(snackbarMessage = "Задача удалена") }
        }
    }

    fun importTasks(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val importedTasks = importTasksUseCase(context)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    snackbarMessage = "Импортировано ${importedTasks.size} задач"
                )
            }
        }
    }

    fun setCompletedColorEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.setCompletedTaskColorEnabled(enabled)
        }
    }

    fun clearSnackbar() {
        _uiState.update { it.copy(snackbarMessage = null) }
    }
}