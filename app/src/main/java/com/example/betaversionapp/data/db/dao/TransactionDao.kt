package com.example.betaversionapp.data.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.betaversionapp.data.db.entities.Transaction

@Dao
interface TransactionDao {
    @Upsert
    suspend fun upsert(transaction: Transaction)
    @Delete
    suspend fun delete(transaction: Transaction)

    @Query("SELECT COALESCE(SUM(CASE WHEN is_income = 1 THEN amount ELSE -amount END), 0) FROM transactions")
    fun getTotalAmount(): LiveData<Long>?

    @Query("SELECT * FROM transactions ORDER BY date_Long DESC, id DESC")
    fun getAllTransactions(): LiveData<List<Transaction>>?

    @Query("SELECT * FROM transactions WHERE id = :categoryId ORDER BY date_Long DESC, id DESC")
    fun getTransactionsByCategory(categoryId: Long): LiveData<List<Transaction>>?

    @Query("SELECT SUM(amount) FROM transactions WHERE category_id = :categoryId")
    suspend fun getSumByCategoryId(categoryId: Long): Long?
    @Query("SELECT * FROM transactions WHERE is_income = :isIncome ORDER BY date_Long DESC, id DESC")
    fun getTransactionsByIsIncome(isIncome: Boolean): LiveData<List<Transaction>>?
}