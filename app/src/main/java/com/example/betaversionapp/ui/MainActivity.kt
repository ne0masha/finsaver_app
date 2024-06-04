package com.example.betaversionapp.ui

import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.betaversionapp.AppDelegate
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.entities.Transaction
import com.example.betaversionapp.ui.listeners.UpsertDialogListener
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        val viewModel = (application as AppDelegate).viewModel
        loadFragment(TransactionsListFragment(viewModel)) // Load the default fragment



        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        navView.selectedItemId = R.id.list_screen_btn
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.list_screen_btn -> {
                    navView.menu.findItem(R.id.pie_screen_btn).isEnabled = true
                    loadFragment(TransactionsListFragment(viewModel))
                    true
                }
                R.id.pie_screen_btn -> {
                    item.isEnabled = false
                    loadFragment(PieChartFragment(viewModel))
                    true

                }
                R.id.plot_screen_btn -> {
                    Toast.makeText(this, "The screen is still building, keep scrolling", Toast.LENGTH_SHORT).show()
                    Log.d("btn", "toast should be showed")
                    true
                }
                else -> false
            }
        }

        val totalAmountTextView = findViewById<TextView>(R.id.totalSummaTextView)

        viewModel.getTotalAmount()?.observe(this) { totalAmount ->
            totalAmountTextView.text = "${String.format("%.2f", totalAmount / 100.0)} $"
        }

    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_holder, fragment).commit()
    }
}