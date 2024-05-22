package com.example.betaversionapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @ColumnInfo(name = "is_income")
    var isIncome: Boolean, // True - доход, False - расход

    @ColumnInfo(name = "category_id")
    var categoryId: Long,

    @ColumnInfo(name = "date_Long")
    var date: Long,

    @ColumnInfo(name = "amount")
    var amount: Long,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
