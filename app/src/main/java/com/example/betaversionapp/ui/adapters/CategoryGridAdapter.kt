package com.example.betaversionapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.betaversionapp.R
import com.example.betaversionapp.data.utils.ResourcesUtil
import com.example.betaversionapp.data.db.entities.Category

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

        val iconId = ResourcesUtil.getResourceIdByName(context, category.icon)
        val drawable = ContextCompat.getDrawable(context, iconId)

        categoryIcon.setImageDrawable(drawable)
        categoryName.text = category.name

        return view
    }
}