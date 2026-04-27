package com.mirea.todolistapp.data.repository

import com.mirea.todolistapp.data.local.TaskDao
import com.mirea.todolistapp.data.local.TaskDatabase
import com.mirea.todolistapp.data.local.TaskEntity
import com.mirea.todolistapp.domain.model.Task
import com.mirea.todolistapp.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskRepositoryImpl(
    private val database: TaskDatabase
) : TaskRepository {
    private val taskDao: TaskDao = database.taskDao()

    override fun getAllTasks(): Flow<List<Task>> {
       return taskDao.getAllTasks().map { entities ->
           entities.map { it.toDomain() }
       }
    }

    override suspend fun getTaskById(id: Int): Task? {
        return taskDao.getTaskById(id)?.toDomain()
    }

    override suspend fun addTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        taskDao.updateTask(task.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
       taskDao.deleteTask(task.toEntity())
    }

    override suspend fun deleteAllTasks() {
        taskDao.deleteAllTasks()
    }

    override suspend fun importTasks(tasks: List<Task>) {
        tasks.forEach { task ->
            taskDao.insertTask(task.toEntity())
        }
    }
}

// Extension functions для преобразования
fun TaskEntity.toDomain(): Task = Task(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    title = title,
    description = description,
    isCompleted = isCompleted,
    createdAt = createdAt
)