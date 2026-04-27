package com.mirea.todolistapp.utils

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mirea.todolistapp.domain.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TaskImporter {
    data class JsonTask(
        val title: String,
        val description: String = "",
        val isCompleted: Boolean = false
    )

    suspend fun importFromJson(context: Context): List<Task> = withContext(Dispatchers.IO) {
        try {
            val jsonString = context.assets.open("tasks.json")
                .bufferedReader()
                .use { it.readText() }

            val type =  object : TypeToken<List<JsonTask>>() {}.type
            val jsonTasks: List<JsonTask> = Gson().fromJson(jsonString, type)

            jsonTasks.mapIndexed { index, jsonTask ->
                Task(
                    id = 0,
                    title = jsonTask.title,
                    description = jsonTask.description,
                    isCompleted = jsonTask.isCompleted,
                    createdAt = System.currentTimeMillis() - (index * 1000L)
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}