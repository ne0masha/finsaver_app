package com.example.betaversionapp.data.repositories

import com.example.betaversionapp.data.db.AppDatabase
import com.example.betaversionapp.data.db.entities.Transaction

class DataBaseRepository(
    private val db: AppDatabase
) {
    suspend fun upsert(transaction: Transaction) = db.getTransactionDao().upsert(transaction)
    suspend fun delete(transaction: Transaction) = db.getTransactionDao().delete(transaction)
    fun getTotalAmount() = db.getTransactionDao().getTotalAmount()
    fun getAllTransactions() = db.getTransactionDao().getAllTransactions()
    fun getTransactionsByCategory(categoryId: Long) = db.getTransactionDao().getTransactionsByCategory(categoryId)


    suspend fun getCategoryById(categoryId: Long) = db.getCategoryDao().getCategoryById(categoryId)
    suspend fun getCategoryByIsIncome(isIncome: Boolean) = db.getCategoryDao().getCategoryByIsIncome(isIncome)
}