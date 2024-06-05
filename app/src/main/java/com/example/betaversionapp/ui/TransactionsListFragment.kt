package com.example.betaversionapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.betaversionapp.AppViewModel
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.entities.Category
import com.example.betaversionapp.data.db.entities.Transaction
import com.example.betaversionapp.data.utils.ResourcesUtil
import com.example.betaversionapp.ui.adapters.TransactionsListAdapter
import com.example.betaversionapp.ui.listeners.CategorySelectionListener
import com.example.betaversionapp.ui.listeners.UpsertDialogListener


class TransactionsListFragment(
    private val viewModel: AppViewModel
) : Fragment(R.layout.fragment_list), CategorySelectionListener {



    private lateinit var showAllButton: Button
    private lateinit var showIncomesButton: Button
    private lateinit var showExpensesButton: Button

    private lateinit var chooseCategoryFilterButton: LinearLayout
    private lateinit var resetCategoryFilterButton: Button

    private var iconCategoryFilter: ImageView? = null
    private var textCategoryFilter: TextView? = null

    private lateinit var addExpenseButton: Button
    private lateinit var addIncomeButton: Button

    private var showIncome: Boolean? = null
    private var showCategory: Long? = null

    private lateinit var adapter: TransactionsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = TransactionsListAdapter(requireActivity() as AppCompatActivity, emptyList(), viewModel)
        val operationsRecyclerView = view.findViewById<RecyclerView>(R.id.operationsRecyclerView)
        operationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        operationsRecyclerView.adapter = adapter
        textCategoryFilter = view.findViewById(R.id.textCategoryFilter)
        iconCategoryFilter = view.findViewById(R.id.iconCategoryFilter)

        showAllButton = view.findViewById(R.id.showAllButton)
        showIncomesButton = view.findViewById(R.id.showIncomesButton)
        showExpensesButton = view.findViewById(R.id.showExpensesButton)


        fun resetButtonStyles() {
            showAllButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_default))
            showIncomesButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_default))
            showExpensesButton.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_default))
        }

        fun setSelectedButton(button: Button) {
            button.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.button_pressed))
        }

        // Устанавливаем кнопку "Все" выбранной при старте
        resetButtonStyles()
        setSelectedButton(showAllButton)
        showIncome = null
        updateTransactionsList(showIncome, showCategory)


        showAllButton.setOnClickListener {
            if (showCategory == null) {
                showIncome = null
                updateTransactionsList(showIncome, showCategory)
                resetButtonStyles()
                setSelectedButton(showAllButton)
            } else {
                Toast.makeText(requireContext(), "Очистите фильтр по категориям", Toast.LENGTH_SHORT).show()
            }
        }
        showIncomesButton.setOnClickListener {
            if (showCategory == null) {
                showIncome = true
                updateTransactionsList(showIncome, showCategory)
                resetButtonStyles()
                setSelectedButton(showIncomesButton)
            } else {
                Toast.makeText(requireContext(), "Очистите фильтр по категориям", Toast.LENGTH_SHORT).show()
            }
        }
        showExpensesButton.setOnClickListener {
            if (showCategory == null) {
                showIncome = false
                updateTransactionsList(showIncome, showCategory)
                resetButtonStyles()
                setSelectedButton(showExpensesButton)
            } else {
                Toast.makeText(requireContext(), "Очистите фильтр по категориям", Toast.LENGTH_SHORT).show()
            }
        }

        chooseCategoryFilterButton = view.findViewById(R.id.chooseCategoryFilterButton)
        resetCategoryFilterButton = view.findViewById(R.id.resetCategoryFilter)

        chooseCategoryFilterButton.setOnClickListener {
            resetButtonStyles()
            val dialog = CategorySelectionDialog.newInstance(showIncome, viewModel)
            dialog.show(parentFragmentManager, "CategorySelectionDialog")
            parentFragmentManager.executePendingTransactions()
            dialog.listener = this
        }

        resetCategoryFilterButton.setOnClickListener {
            showCategory = null
            updateTransactionsList(showIncome, showCategory)
            textCategoryFilter?.text = getString(R.string.placeholder_choosen_category)
            iconCategoryFilter?.setImageResource(R.drawable.question_mark)
        }


        addExpenseButton = view.findViewById(R.id.addExpenseButton)
        addIncomeButton = view.findViewById(R.id.addIncomeButton)
        addExpenseButton.setOnClickListener {
            TransactionUpsertDialog(
                requireActivity() as AppCompatActivity,
                viewModel,
                object : UpsertDialogListener {
                    override fun onAddButtonClicked(item: Transaction) {
                        viewModel.upsert(item)
                    }
                },
                null,
                false
            ).show()
        }

        addIncomeButton.setOnClickListener {
            TransactionUpsertDialog(
                requireActivity() as AppCompatActivity,
                viewModel,
                object : UpsertDialogListener {
                    override fun onAddButtonClicked(item: Transaction) {
                        viewModel.upsert(item)
                    }
                },
                null,
                true
            ).show()
        }

    }

    override fun onCategorySelected(category: Category) {
        showCategory = category.id
        Log.d("onCategorySelected", "category id = $showCategory")
        textCategoryFilter?.text = category.name
        val iconId = ResourcesUtil.getResourceIdByName(requireContext(), category.icon)
        val drawable = ContextCompat.getDrawable(requireContext(), iconId)
        iconCategoryFilter?.setImageDrawable(drawable)
        updateTransactionsList(showIncome, showCategory)

    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateTransactionsList(showIncome: Boolean?, showCategory: Long?) {
        val transactionsLiveData = if (showCategory != null) {
            viewModel.getTransactionsByCategory(showCategory)
        } else {
            if (showIncome == null) {
                viewModel.getAllTransactions()
            } else {
                viewModel.getTransactionsByIsIncome(showIncome)
            }
        }
        Log.d("updateTransactionsList", "$transactionsLiveData, $showCategory, $showIncome")
        transactionsLiveData?.observe(viewLifecycleOwner) { transactions ->
            adapter.items = transactions
            adapter.notifyDataSetChanged()
        }
    }
}
