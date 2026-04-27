package com.mirea.personaldiary.data

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DiaryEntry(
    val fileName: String,
    val title: String,
    val content: String,
    val timestamp: Long
) {
    val displayDate: String
        get() = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault())
            .format(Date(timestamp))

    val previewText: String
        get() = if (content.length > 40) content.take(40) + "..." else content

    companion object {
        fun fromFile(file: File): DiaryEntry? {
            return try {
                val content = file.readText()
                val fileName = file.name

                val parts = fileName.removeSuffix(".txt").split("_", limit = 2)
                val timestamp = parts[0].toLongOrNull() ?: file.lastModified()

                val rawTitle = if (parts.size > 1) parts[1] else ""
                val title = rawTitle.replace("_", " ")

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
    }
}