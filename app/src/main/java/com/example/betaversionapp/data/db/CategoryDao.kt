package com.example.betaversionapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.betaversionapp.data.db.entities.Category

@Dao
interface CategoryDao {
    @Insert
    suspend fun insertCategory(category: Category)

//    @Query("SELECT * FROM categories")
//    fun getAllCategories(): LiveData<List<Category>>

    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Long): Category?
}
