package com.example.betaversionapp.ui.listeners

import com.example.betaversionapp.data.db.entities.Category

interface CategorySelectionListener {
    fun onCategorySelected(category: Category)
}
