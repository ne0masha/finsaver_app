package com.example.betaversionapp

import android.app.Application
import com.example.betaversionapp.data.db.AppDatabase
import com.example.betaversionapp.data.repositories.TransactionsListRepository

class AppDelegate : Application() {
    val database: AppDatabase by lazy { AppDatabase(this) }
    val repository: TransactionsListRepository by lazy { TransactionsListRepository(database) }
}
