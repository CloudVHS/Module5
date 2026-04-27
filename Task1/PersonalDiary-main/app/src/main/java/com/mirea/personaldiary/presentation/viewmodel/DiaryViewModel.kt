package com.mirea.personaldiary.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mirea.personaldiary.data.DiaryEntry
import com.mirea.personaldiary.data.DiaryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class DiaryUiState(
    val entries: List<DiaryEntry> = emptyList(),
    val isLoading: Boolean = false,
    val selectedEntry: DiaryEntry? = null,
    val isEditing: Boolean = false
)

class DiaryViewModel(
    private val repository: DiaryRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(DiaryUiState())
    val uiState: StateFlow<DiaryUiState> = _uiState.asStateFlow()

    init {
        loadAllEntries()
    }

    private fun loadAllEntries() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val entries = withContext(Dispatchers.IO) {
                repository.loadAllEntries()
            }
            _uiState.update { it.copy(entries = entries, isLoading = false) }
        }
    }

    fun saveEntry(title: String, content: String) {
        viewModelScope.launch {
            val newEntry = withContext(Dispatchers.IO) {
                repository.saveEntry(title, content)
            }
            newEntry?.let {
                _uiState.update { state ->
                    state.copy(
                        entries = listOf(it) + state.entries
                    )
                }
            }
        }
    }

    fun updateEntry(fileName: String, title: String, content: String) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.updateEntry(fileName, title, content)
            }
            if (success) {
                _uiState.update { state ->
                    state.copy(
                        entries = state.entries.map { entry ->
                            if (entry.fileName == fileName) {
                                entry.copy(title = title, content = content)
                            } else entry
                        }
                    )
                }
            }
        }
    }

    fun deleteEntry(fileName: String) {
        viewModelScope.launch {
            val success = withContext(Dispatchers.IO) {
                repository.deleteEntry(fileName)
            }
            if (success) {
                _uiState.update { state ->
                    state.copy(
                        entries = state.entries.filter { it.fileName != fileName }
                    )
                }
            }
        }
    }
}