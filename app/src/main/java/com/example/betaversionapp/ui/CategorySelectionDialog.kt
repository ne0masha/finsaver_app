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
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.DataBaseRepository
import com.example.betaversionapp.ui.adapters.CategoryGridAdapter
import com.example.betaversionapp.ui.listeners.CategorySelectionListener
import kotlinx.coroutines.launch

class CategorySelectionDialog : DialogFragment() {


    var listener: CategorySelectionListener? = null
    private var isIncome: Boolean? = null
    private lateinit var repository: DataBaseRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CategorySelectionListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isIncome = if (it.containsKey(ARG_IS_INCOME)) { it.getBoolean(ARG_IS_INCOME) } else { null }
            repository = (requireActivity().applicationContext as AppInit).repository
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_category_selection, container, false)

        val gridView = view.findViewById<GridView>(R.id.categories_grid)

        lifecycleScope.launch {
            val categories = if (isIncome == null) {
                repository.getAllCategories()
            } else {
                repository.getCategoryByIsIncome(isIncome!!)
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
        private const val ARG_IS_INCOME = "is_income"

        @JvmStatic
        fun newInstance(isIncome: Boolean?): CategorySelectionDialog {
            val dialog = CategorySelectionDialog()
            val args = Bundle()
            if (isIncome != null) {
                args.putBoolean(ARG_IS_INCOME, isIncome)
            }
            dialog.arguments = args
            return dialog
        }
    }
}
