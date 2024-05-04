package com.example.betaversionapp.ui

import androidx.lifecycle.ViewModel
import com.example.betaversionapp.data.db.entities.Transaction
import com.example.betaversionapp.data.repositories.TransactionsListRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TransactionsListViewModel(
    private val repository: TransactionsListRepository
): ViewModel() {
    fun upsert(transaction: Transaction) = CoroutineScope(Dispatchers.IO).launch {
        repository.upsert(transaction)
    }

    fun delete(transaction: Transaction) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(transaction)
    }

    fun getTransactionById(transactionId: Long) = CoroutineScope(Dispatchers.IO).launch {
        repository.getTransactionById(transactionId)
    }

    fun getTotalAmount() = repository.getTotalAmount()

    fun getAllTransactions() = repository.getAllTransactions()
}