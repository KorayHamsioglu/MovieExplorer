package com.example.obssproject.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.obssproject.db.MovieDatabase

class SharedViewModelProviderFactory(
    val movieDatabase: MovieDatabase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedViewModel(movieDatabase) as T
    }
}