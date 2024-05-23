package com.example.betaversionapp.other

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.entities.Category
import com.example.betaversionapp.data.repositories.TransactionsListRepository

class CategoryGridAdapter(
    private val context: Context,
    private val categories: List<Category>,
    ) : BaseAdapter() {

    override fun getCount(): Int = categories.size

    override fun getItem(position: Int): Any = categories[position]

    override fun getItemId(position: Int): Long = categories[position].id

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_category, parent, false)

        val category = categories[position]
        val categoryIcon = view.findViewById<ImageView>(R.id.category_icon)
        val categoryName = view.findViewById<TextView>(R.id.category_name)

        categoryIcon.setImageResource(category.icon)
        categoryName.text = category.name

        return view
    }
}