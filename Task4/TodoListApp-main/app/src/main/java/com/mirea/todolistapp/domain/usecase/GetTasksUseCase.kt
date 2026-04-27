package com.mirea.todolistapp.domain.usecase

import com.mirea.todolistapp.domain.model.Task
import com.mirea.todolistapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasksUseCase(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<List<Task>> = repository.getAllTasks()
}