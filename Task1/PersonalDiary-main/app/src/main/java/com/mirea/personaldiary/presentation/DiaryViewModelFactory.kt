package com.mirea.personaldiary.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mirea.personaldiary.data.DiaryRepository
import com.mirea.personaldiary.presentation.viewmodel.DiaryViewModel

class DiaryViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DiaryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DiaryViewModel(DiaryRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}