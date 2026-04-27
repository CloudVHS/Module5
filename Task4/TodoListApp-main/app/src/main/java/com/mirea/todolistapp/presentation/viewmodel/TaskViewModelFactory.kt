package com.mirea.todolistapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mirea.todolistapp.data.preferences.PreferencesRepository
import com.mirea.todolistapp.domain.usecase.*
class TaskViewModelFactory(
    private val getTasksUseCase: GetTasksUseCase,
    private val addTaskUseCase: AddTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val importTasksUseCase: ImportTasksUseCase,
    private val preferencesRepository: PreferencesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(
                getTasksUseCase,
                addTaskUseCase,
                updateTaskUseCase,
                deleteTaskUseCase,
                importTasksUseCase,
                preferencesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}