package com.mirea.todolistapp.domain.usecase

import com.mirea.todolistapp.domain.model.Task
import com.mirea.todolistapp.domain.repository.TaskRepository

class UpdateTaskUseCase (
    private val repository: TaskRepository
) {
    suspend operator fun invoke(task: Task) {
        repository.updateTask(task)
    }
}