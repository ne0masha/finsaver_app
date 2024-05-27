package com.example.betaversionapp

import android.app.Application
import com.example.betaversionapp.data.db.AppDatabase
import com.example.betaversionapp.data.db.DataBaseRepository
import com.example.betaversionapp.ui.TransactionsListViewModel
import com.example.betaversionapp.ui.TransactionsListViewModelFactory

class AppDelegate : Application() {
    val database: AppDatabase by lazy { AppDatabase(this) }
    val repository: DataBaseRepository by lazy { DataBaseRepository(database) }
    val viewModel: TransactionsListViewModel by lazy {
        TransactionsListViewModelFactory(repository).create(TransactionsListViewModel::class.java)
    }
}
