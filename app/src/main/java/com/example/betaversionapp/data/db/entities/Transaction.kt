package com.example.betaversionapp.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @ColumnInfo(name = "is_income")
    val isIncome: Boolean, // True - доход, False - расход
    @ColumnInfo(name = "category_id")
    val categoryId: Int,
    @ColumnInfo(name = "date_Long")
    val date: Long,
    @ColumnInfo(name = "amount")
    val amount: Long,
    // сумма хранится в копейках, при отображении делится на 100
    // Нужно, чтобы не получались тысячные доли валюты.
) {

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
}
