package com.example.betaversionapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betaversionapp.ApptViewModel
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.entities.Transaction
import com.example.betaversionapp.ui.adapters.TransactionsListAdapter
import com.example.betaversionapp.ui.listeners.UpsertDialogListener


class TransactionsListFragment(
    private val viewModel: ApptViewModel
) : Fragment(R.layout.fragment_list) {


    private lateinit var addExpenseButton: Button
    private lateinit var addIncomeButton: Button
    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = TransactionsListAdapter(requireActivity() as AppCompatActivity, emptyList(), viewModel)

        val operationsRecyclerView = view.findViewById<RecyclerView>(R.id.operationsRecyclerView)
        operationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        operationsRecyclerView.adapter = adapter

        viewModel.getAllTransactions()?.observe(viewLifecycleOwner) { transactions ->
            adapter.items = transactions
            adapter.notifyDataSetChanged()
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
