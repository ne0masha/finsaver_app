package com.example.betaversionapp.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.entities.Category
import com.example.betaversionapp.data.repositories.TransactionsListRepository
import com.example.betaversionapp.other.CategoryGridAdapter
import kotlinx.coroutines.launch

class CategorySelectionDialog(
    private val repository: TransactionsListRepository,
    private val isIncome: Boolean
) : DialogFragment() {

    interface CategorySelectionListener {
        fun onCategorySelected(category: Category)
    }

    private var listener: CategorySelectionListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CategorySelectionListener) {
            listener = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_category_selection, container, false)

        val gridView = view.findViewById<GridView>(R.id.categories_grid)

        lifecycleScope.launch {
            val categories = repository.getCategoryByIsIncome(isIncome)
            gridView.adapter = CategoryGridAdapter(requireContext(), categories)
        }

        gridView.setOnItemClickListener { _, _, position, _ ->
            lifecycleScope.launch {
                val categories = repository.getCategoryByIsIncome(isIncome)
                val selectedCategory = categories[position]
                listener?.onCategorySelected(selectedCategory)
                //dismiss()
            }
        }

        view.findViewById<Button>(R.id.SaveCategoryButton).setOnClickListener { dismiss() }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(repository: TransactionsListRepository, isIncome: Boolean) = CategorySelectionDialog(repository, isIncome)
    }
}
