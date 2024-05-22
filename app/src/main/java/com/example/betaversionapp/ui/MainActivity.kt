package com.example.betaversionapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.betaversionapp.AppDelegate
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.entities.Transaction
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        loadFragment(TransactionsListFragment()) // Load the default fragment

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.selectedItemId = R.id.list_screen_btn
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.list_screen_btn -> {
                    loadFragment(TransactionsListFragment())
                    true
                }
                R.id.pie_screen_btn -> {
                    loadFragment(PieChartFragment())
                    true
                }
                R.id.plot_screen_btn -> {
                    Toast.makeText(this, "The screen is still building, keep scrolling", Toast.LENGTH_SHORT).show()
                    Log.d("btn", "toast should be showed")
                    true
                }
                // Add cases for other fragments if needed
                else -> false
            }
        }

        val appDelegate = application as AppDelegate
        val factory = TransactionsListViewModelFactory(appDelegate.repository)
        val viewModel = ViewModelProvider(this, factory)[TransactionsListViewModel::class.java]
        val addExpenseButton = findViewById<Button>(R.id.addExpenseButton)
        val addIncomeButton = findViewById<Button>(R.id.addIncomeButton)
        val totalAmountTextView = findViewById<TextView>(R.id.totalSummaTextView)

        viewModel.getTotalAmount()?.observe(this) { totalAmount ->
            totalAmountTextView.text = "${String.format("%.2f", totalAmount / 100.0)} $"
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


    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit()
    }
}