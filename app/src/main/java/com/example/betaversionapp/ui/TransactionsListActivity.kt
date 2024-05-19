package com.example.betaversionapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.AppDatabase
import com.example.betaversionapp.data.db.entities.Transaction
import com.example.betaversionapp.data.repositories.TransactionsListRepository
import com.example.betaversionapp.other.TransactionsListAdapter


class TransactionsListActivity : AppCompatActivity() {
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val database = AppDatabase(this)
        val repository = TransactionsListRepository(database)
        val factory = TransactionsListViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[TransactionsListViewModel::class.java]

        val adapter = TransactionsListAdapter(listOf(), viewModel)
        val operationsRecyclerView = findViewById<RecyclerView>(R.id.operationsRecyclerView)

        val addExpenseButton = findViewById<Button>(R.id.addExpenseButton)
        val addIncomeButton = findViewById<Button>(R.id.addIncomeButton)

        operationsRecyclerView.layoutManager = LinearLayoutManager(this)
        operationsRecyclerView.adapter = adapter

        val totalAmountTextView = findViewById<TextView>(R.id.totalSummaTextView)
        viewModel.getTotalAmount()?.observe(this) { totalAmount ->
            totalAmountTextView.text = "${String.format("%.2f", totalAmount / 100.0)} $"
        }

        viewModel.getAllTransactions()?.observe(this) { transactions ->
            adapter.items = transactions
            adapter.notifyDataSetChanged()
        }


        addExpenseButton.setOnClickListener {
            TransactionUpsertDialog(
                this,
                object : UpsertDialogListener {
                    override fun onAddButtonClicked(item: Transaction) {
                        viewModel.upsert(item)
                    }
                },
                null,
                false
            ).show()
        }

        addIncomeButton.setOnClickListener {
            TransactionUpsertDialog(
                this,
                object : UpsertDialogListener {
                    override fun onAddButtonClicked(item: Transaction) {
                        viewModel.upsert(item)
                    }
                },
                null,
                true
            ).show()
        }

    }
}