package com.example.betaversionapp

import android.app.Application
import com.example.betaversionapp.data.db.AppDatabase
import com.example.betaversionapp.data.db.DataBaseRepository

class AppDelegate : Application() {
    val database: AppDatabase by lazy { AppDatabase(this) }
    val repository: DataBaseRepository by lazy { DataBaseRepository(database) }
    val viewModel: ApptViewModel by lazy {
        ApptViewModelFactory(repository).create(ApptViewModel::class.java)
    }
}
