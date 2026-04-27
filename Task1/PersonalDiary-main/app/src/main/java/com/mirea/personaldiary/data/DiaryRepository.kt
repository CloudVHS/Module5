package com.mirea.personaldiary.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class DiaryRepository (private val context: Context) {
    private val diaryDir: File
        get() = context.filesDir

    suspend fun loadAllEntries(): List<DiaryEntry> = withContext(Dispatchers.IO) {
        diaryDir.listFiles { file -> file.extension == "txt" }
            ?.mapNotNull { file -> DiaryEntry.fromFile(file) }
            ?.sortedByDescending { it.timestamp }
            ?: emptyList()
    }

    suspend fun saveEntry(title: String, content: String): DiaryEntry? = withContext(Dispatchers.IO) {
        val timestamp = System.currentTimeMillis()
        val safeTitle = title.replace(" ", "_").replace("[^a-zA-Z0-9_]".toRegex(), "")
        val fileName = if (safeTitle.isNotEmpty()) {
            "${timestamp}_${safeTitle}.txt"
        } else {
            "${timestamp}.txt"
        }

        val file = File(diaryDir, fileName)

        return@withContext try {
            file.writeText(content)
            DiaryEntry(
                fileName = fileName,
                title = title,
                content = content,
                timestamp = timestamp
            )
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateEntry(fileName: String, title: String, content: String): Boolean = withContext(
        Dispatchers.IO) {
        val file = File(diaryDir, fileName)
        return@withContext try {
            file.writeText(content)
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteEntry(fileName: String): Boolean = withContext(Dispatchers.IO) {
        val file = File(diaryDir, fileName)
        return@withContext try {
            file.delete()
        } catch (e: Exception) {
            false
        }
    }
}