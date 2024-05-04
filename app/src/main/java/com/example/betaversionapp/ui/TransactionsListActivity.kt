package com.example.betaversionapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.betaversionapp.data.db.AppDatabase
import com.example.betaversionapp.data.repositories.TransactionsListRepository

class TransactionsListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout. ...)

        val database = AppDatabase(this)
        val repository = TransactionsListRepository(database)
        val factory = TransactionsListViewModelFactory(repository)

        val viewModel: TransactionsListViewModel by viewModels()

    }
}