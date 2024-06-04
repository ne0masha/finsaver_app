package com.example.betaversionapp

import android.app.Application
import com.example.betaversionapp.data.db.AppDatabase
import com.example.betaversionapp.data.db.DataBaseRepository

class AppInit : Application() {
    val database: AppDatabase by lazy { AppDatabase(this) }
    val repository: DataBaseRepository by lazy { DataBaseRepository(database) }
    val viewModel: AppViewModel by lazy {
        AppViewModelFactory(repository).create(AppViewModel::class.java)
    }
}
