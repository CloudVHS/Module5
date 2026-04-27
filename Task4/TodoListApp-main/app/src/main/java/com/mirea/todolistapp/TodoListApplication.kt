package com.mirea.todolistapp

import android.app.Application
import com.mirea.todolistapp.data.local.TaskDatabase
import com.mirea.todolistapp.data.preferences.PreferencesRepository
import com.mirea.todolistapp.data.repository.TaskRepositoryImpl
import com.mirea.todolistapp.domain.repository.TaskRepository
import com.mirea.todolistapp.domain.usecase.AddTaskUseCase
import com.mirea.todolistapp.domain.usecase.DeleteTaskUseCase
import com.mirea.todolistapp.domain.usecase.GetTasksUseCase
import com.mirea.todolistapp.domain.usecase.ImportTasksUseCase
import com.mirea.todolistapp.domain.usecase.UpdateTaskUseCase
import com.mirea.todolistapp.utils.TaskImporter

class TodoListApplication : Application() {
    lateinit var taskRepository: TaskRepository
    lateinit var preferencesRepository: PreferencesRepository
    lateinit var getTasksUseCase: GetTasksUseCase
    lateinit var addTaskUseCase: AddTaskUseCase
    lateinit var updateTaskUseCase: UpdateTaskUseCase
    lateinit var deleteTaskUseCase: DeleteTaskUseCase
    lateinit var importTasksUseCase: ImportTasksUseCase

    override fun onCreate() {
        super.onCreate()

        val database = TaskDatabase.getDatabase(this)
        taskRepository = TaskRepositoryImpl(database)
        preferencesRepository = PreferencesRepository(this)

        val taskImporter = TaskImporter()

        getTasksUseCase = GetTasksUseCase(taskRepository)
        addTaskUseCase = AddTaskUseCase(taskRepository)
        updateTaskUseCase = UpdateTaskUseCase(taskRepository)
        deleteTaskUseCase = DeleteTaskUseCase(taskRepository)
        importTasksUseCase = ImportTasksUseCase(taskRepository, taskImporter)
    }
}