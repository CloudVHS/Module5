package com.mirea.todolistapp.domain.usecase

import android.content.Context
import com.mirea.todolistapp.domain.model.Task
import com.mirea.todolistapp.domain.repository.TaskRepository
import com.mirea.todolistapp.utils.TaskImporter

class ImportTasksUseCase(
    private val repository: TaskRepository,
    private val taskImporter: TaskImporter
) {
    suspend operator fun invoke(context: Context): List<Task> {
        val tasks = taskImporter.importFromJson(context)
        repository.importTasks(tasks)
        return tasks
    }
}