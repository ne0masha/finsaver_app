package com.example.betaversionapp

import androidx.lifecycle.ViewModel
import com.example.betaversionapp.data.db.entities.Category
import com.example.betaversionapp.data.db.entities.Transaction
import com.example.betaversionapp.data.db.DataBaseRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: DataBaseRepository
): ViewModel() {

    fun upsert(transaction: Transaction) = CoroutineScope(Dispatchers.IO).launch {
        repository.upsert(transaction)
    }

    fun delete(transaction: Transaction) = CoroutineScope(Dispatchers.IO).launch {
        repository.delete(transaction)
    }

    fun getTotalAmount() = repository.getTotalAmount()

    fun getAllTransactions() = repository.getAllTransactions()

    suspend fun getCategoryById(categoryId: Long): Category? = repository.getCategoryById(categoryId)

    suspend fun getSumByCategoryId(categoryId: Long): Long? = repository.getSumByCategoryId(categoryId)
}

