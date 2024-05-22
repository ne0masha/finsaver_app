package com.example.betaversionapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betaversionapp.AppDelegate
import com.example.betaversionapp.R
import com.example.betaversionapp.other.TransactionsListAdapter


class TransactionsListFragment : Fragment(R.layout.fragment_list) {

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appDelegate = requireActivity().application as AppDelegate
        val factory = TransactionsListViewModelFactory(appDelegate.repository)
        val viewModel = ViewModelProvider(this, factory)[TransactionsListViewModel::class.java]

        val adapter = TransactionsListAdapter(listOf(), viewModel)
        val operationsRecyclerView = view.findViewById<RecyclerView>(R.id.operationsRecyclerView)

        operationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        operationsRecyclerView.adapter = adapter

        viewModel.getAllTransactions()?.observe(viewLifecycleOwner) { transactions ->
            adapter.items = transactions
            adapter.notifyDataSetChanged()
        }

    }
}