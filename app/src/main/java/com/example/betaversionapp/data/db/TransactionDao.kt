package com.example.betaversionapp.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Upsert
    suspend fun upsertTransaction(transaction: Transaction)
    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @Query("SELECT * FROM `Transaction` WHERE id = :transactionId")
    suspend fun getTransactionById(transactionId: Long): Transaction?

    @Query("SELECT COALESCE(SUM(CASE WHEN isIncome = 1 THEN amount ELSE -amount END), 0) FROM `Transaction`")
    fun getTotalAmount(): Flow<Long>

    @Query("SELECT * FROM `Transaction` ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<Transaction>>
}