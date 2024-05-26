package com.example.betaversionapp.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.betaversionapp.data.db.entities.Category

@Dao
interface CategoryDao {
    @Insert
    fun insertCategory(category: Category)
    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>

    @Query("SELECT * FROM categories WHERE is_income = :isIncome")
    suspend fun getCategoryByIsIncome(isIncome: Boolean): List<Category>
    @Query("SELECT * FROM categories WHERE id = :categoryId LIMIT 1")
    suspend fun getCategoryById(categoryId: Long): Category

}
