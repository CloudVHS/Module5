package com.mirea.todolistapp.domain.repository

import com.mirea.todolistapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<Task>>
    suspend fun getTaskById(id: Int): Task?
    suspend fun addTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(task: Task)
    suspend fun deleteAllTasks()
    suspend fun importTasks(tasks: List<Task>)
}