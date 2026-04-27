package com.mirea.todolistapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCES_NAME = "todo_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

class PreferencesRepository(private val context: Context) {
    companion object {
        private val COMPLETED_TASK_COLOR_ENABLED = booleanPreferencesKey("completed_task_color_enabled")
    }

    val isCompletedTaskColorEnabled: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[COMPLETED_TASK_COLOR_ENABLED] ?: false
        }

    suspend fun setCompletedTaskColorEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[COMPLETED_TASK_COLOR_ENABLED] = enabled
        }
    }
}
