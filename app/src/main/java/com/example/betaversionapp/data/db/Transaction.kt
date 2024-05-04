package com.example.betaversionapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction(
    val isIncome: Boolean, // True - доход, False - расход
    val categoryId: Int,
    val date: Long,
    val amount: Long, // сумма хранится в копейках, при отображении делится на 100
    // Нужно, чтобы не получались тысячные доли валюты.
    // TODO: сделать целую и дробную часть операции разными переменными, чтобы увеличить лимит

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
