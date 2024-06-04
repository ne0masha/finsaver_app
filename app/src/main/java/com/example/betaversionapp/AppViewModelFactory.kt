package com.example.betaversionapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.betaversionapp.data.db.DataBaseRepository

@Suppress("UNCHECKED_CAST")
class AppViewModelFactory(
    private val repository: DataBaseRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AppViewModel(repository) as T
    }
}