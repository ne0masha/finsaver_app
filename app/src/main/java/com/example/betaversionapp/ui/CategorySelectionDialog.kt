package com.example.betaversionapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.betaversionapp.AppInit
import com.example.betaversionapp.AppViewModel
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.DataBaseRepository
import com.example.betaversionapp.ui.adapters.CategoryGridAdapter
import com.example.betaversionapp.ui.listeners.CategorySelectionListener
import kotlinx.coroutines.launch

class CategorySelectionDialog(
    private val isIncome: Boolean?,
    private val viewModel: AppViewModel
) : DialogFragment() {

    var listener: CategorySelectionListener? = null

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
            val categories = if (isIncome == null) {
                viewModel.getAllCategories()
            } else {
                viewModel.getCategoryByIsIncome(isIncome)
            }
            gridView.adapter = CategoryGridAdapter(requireContext(), categories)

            gridView.setOnItemClickListener { _, _, position, _ ->
                val selectedCategory = categories[position]
                listener?.onCategorySelected(selectedCategory)
                dismiss()
            }
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(isIncome: Boolean?, viewModel: AppViewModel): CategorySelectionDialog {
            return CategorySelectionDialog(isIncome, viewModel)
        }
    }
}
