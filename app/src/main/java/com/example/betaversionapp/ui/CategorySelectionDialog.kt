package com.example.betaversionapp.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.betaversionapp.AppDelegate
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.entities.Category
import com.example.betaversionapp.data.repositories.TransactionsListRepository
import com.example.betaversionapp.other.CategoryGridAdapter
import kotlinx.coroutines.launch

class CategorySelectionDialog : DialogFragment() {

    interface CategorySelectionListener {
        fun onCategorySelected(category: Category)
    }

    var listener: CategorySelectionListener? = null
    private var isIncome: Boolean = false
    private lateinit var repository: TransactionsListRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is CategorySelectionListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isIncome = it.getBoolean(ARG_IS_INCOME)
            repository = (requireActivity().applicationContext as AppDelegate).repository
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.dialog_category_selection, container, false)

        val gridView = view.findViewById<GridView>(R.id.categories_grid)

        lifecycleScope.launch {
            val categories = repository.getCategoryByIsIncome(isIncome)
            Log.d("10", "${categories.size}")
            gridView.adapter = CategoryGridAdapter(requireContext(), categories)

            gridView.setOnItemClickListener { _, _, position, _ ->
                val selectedCategory = categories[position]
                listener?.onCategorySelected(selectedCategory)
                //dismiss()
            }
        }

        view.findViewById<Button>(R.id.SaveCategoryButton).setOnClickListener { dismiss() }

        return view
    }

    companion object {
        private const val ARG_IS_INCOME = "is_income"

        @JvmStatic
        fun newInstance(isIncome: Boolean): CategorySelectionDialog {
            val dialog = CategorySelectionDialog()
            val args = Bundle()
            args.putBoolean(ARG_IS_INCOME, isIncome)
            dialog.arguments = args
            return dialog
        }
    }
}
