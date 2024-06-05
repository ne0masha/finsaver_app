package com.example.betaversionapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betaversionapp.AppViewModel
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.entities.Transaction
import com.example.betaversionapp.ui.adapters.TransactionsListAdapter
import com.example.betaversionapp.ui.listeners.UpsertDialogListener


class TransactionsListFragment(
    private val viewModel: AppViewModel
) : Fragment(R.layout.fragment_list) {


    private lateinit var addExpenseButton: Button
    private lateinit var addIncomeButton: Button
    private lateinit var showAllButton: Button
    private lateinit var showIncomesButton: Button
    private lateinit var showExpensesButton: Button

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TransactionsListAdapter(requireActivity() as AppCompatActivity, emptyList(), viewModel)
        val operationsRecyclerView = view.findViewById<RecyclerView>(R.id.operationsRecyclerView)
        operationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        operationsRecyclerView.adapter = adapter

        var showIncome: Boolean? = null
        showAllButton = view.findViewById(R.id.showAllButton)
        showIncomesButton = view.findViewById(R.id.showIncomesButton)
        showExpensesButton = view.findViewById(R.id.showExpensesButton)

        fun updateTransactionsList(showIncome: Boolean?) {
            val transactionsLiveData = if (showIncome == null) {
                viewModel.getAllTransactions()
            } else {
                viewModel.getTransactionsByIsIncome(showIncome)
            }

            transactionsLiveData?.observe(viewLifecycleOwner) { transactions ->
                adapter.items = transactions
                adapter.notifyDataSetChanged()
            }
        }
        updateTransactionsList(showIncome)


        fun resetButtonStyles() {
            showAllButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_default))
            showIncomesButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_default))
            showExpensesButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_default))
        }

        fun setSelectedButton(button: Button) {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_pressed))
        }

        showAllButton.setOnClickListener {
            showIncome = null
            updateTransactionsList(showIncome)
            resetButtonStyles()
            setSelectedButton(showAllButton)
        }
        showIncomesButton.setOnClickListener {
            showIncome = true
            updateTransactionsList(showIncome)
            resetButtonStyles()
            setSelectedButton(showIncomesButton)
        }
        showExpensesButton.setOnClickListener {
            showIncome = false
            updateTransactionsList(showIncome)
            resetButtonStyles()
            setSelectedButton(showExpensesButton)
        }


        addExpenseButton = view.findViewById(R.id.addExpenseButton)
        addIncomeButton = view.findViewById(R.id.addIncomeButton)
        addExpenseButton.setOnClickListener {
            TransactionUpsertDialog(
                requireActivity() as AppCompatActivity,
                viewModel,
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
                requireActivity() as AppCompatActivity,
                viewModel,
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
