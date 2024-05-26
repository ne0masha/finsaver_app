package com.example.betaversionapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.betaversionapp.data.repositories.DataBaseRepository

@Suppress("UNCHECKED_CAST")
class TransactionsListViewModelFactory(
    private val repository: DataBaseRepository
): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionsListViewModel(repository) as T
    }
}