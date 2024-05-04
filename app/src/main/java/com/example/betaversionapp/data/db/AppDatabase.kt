package com.example.betaversionapp.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Transaction::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val transactionDao: TransactionDao
}
