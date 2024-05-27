package com.example.betaversionapp.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.betaversionapp.AppDelegate
import com.example.betaversionapp.R
import com.example.betaversionapp.data.utils.DateConverter
import com.example.betaversionapp.data.utils.ResourcesUtil
import com.example.betaversionapp.data.db.entities.Category
import com.example.betaversionapp.data.db.entities.Transaction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class TransactionUpsertDialog(
    activity: AppCompatActivity,
    private val viewModel: TransactionsListViewModel,
    private var upsertDialogListener: UpsertDialogListener,
    private var transaction: Transaction?,
    private var isIncome: Boolean
): AppCompatDialog(activity), CategorySelectionDialog.CategorySelectionListener {
    private var dateInput: EditText? = null
    private var amountInput: EditText? = null
    private var selectedCategoryId: Long = 0
    private var categoryText: TextView? = null
    private var categoryImage: ImageView? = null
    private val activityContext = activity

    override fun onCategorySelected(category: Category) {
        selectedCategoryId = category.id
        categoryText?.text = category.name
        val iconId = ResourcesUtil.getResourceIdByName(activityContext, category.icon)
        val drawable = ContextCompat.getDrawable(activityContext, iconId)
        categoryImage?.setImageDrawable(drawable)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_transaction_upsert)

        val headerFragment = findViewById<TextView>(R.id.HeaderFragment)
        headerFragment?.text = if (isIncome) "ДОХОД" else "РАСХОД"

        dateInput = findViewById(R.id.date_input)
        amountInput = findViewById(R.id.amount_input)
        categoryText = findViewById(R.id.textCategory)
        categoryImage = findViewById(R.id.iconCategory)

        val cancelButton = findViewById<Button>(R.id.cancel_button)
        val saveButton = findViewById<Button>(R.id.save_button)
        val categoryButton = findViewById<LinearLayout>(R.id.CategoryButton)

        transaction?.let { fillFieldsWithData(it) }

        if (transaction == null) {
            val calendar = Calendar.getInstance()
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
            val initialDate = "$initialDay.${initialMonth + 1}.$initialYear"
            dateInput?.setText(initialDate)

            categoryText?.setText("Выберите категорию")
            categoryImage?.setImageResource(R.drawable.question_mark)

        }


        amountInput?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // No action needed before text changed
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // No action needed on text changed
            }
            override fun afterTextChanged(s: Editable) {
                val str = s.toString()
                val position = str.indexOf(".")
                if (position != -1) {
                    val subStr = str.substring(position) // строка после точки
                    val subStrStart = str.substring(0, position) // строка до точки
                    if (subStr.length > 3 || subStrStart.isEmpty()) {
                        s.delete(
                            s.length - 1,
                            s.length
                        )
                    }
                }
            }
        })

        dateInput?.setOnClickListener { showDatePicker() }

        categoryButton?.setOnClickListener {
            val dialog = CategorySelectionDialog.newInstance(isIncome)
            dialog.show(activityContext.supportFragmentManager, "CategorySelectionDialog")
            activityContext.supportFragmentManager.executePendingTransactions()
            dialog.listener = this
        }

        saveButton?.setOnClickListener {
            val date = DateConverter.dateToLong(dateInput?.text.toString())
            val amount: Long = if (amountInput?.text.toString().isNotBlank()) {
                (amountInput?.text.toString().toDouble() * 100).toLong()
            } else 0

            if (isInputValid(amountInput)) {
                if (transaction == null) {
                    // Создание новой транзакции
                    val item = Transaction(isIncome, selectedCategoryId, date, amount)
                    upsertDialogListener.onAddButtonClicked(item)
                } else {
                    // Обновление существующей транзакции
                    transaction?.let {
                        it.isIncome = isIncome
                        it.categoryId = selectedCategoryId
                        it.date = date
                        it.amount = amount
                        upsertDialogListener.onAddButtonClicked(it)
                    }
                }
                dismiss()
            } else {
                Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton?.setOnClickListener {
            cancel()
        }
    }
    private fun fillFieldsWithData(transaction: Transaction) {
        dateInput?.setText(DateConverter.longToDate(transaction.date))
        amountInput?.setText(String.format("%.2f", transaction.amount / 100.0))

        selectedCategoryId = transaction.categoryId
        CoroutineScope(Dispatchers.IO).launch {
            val category = viewModel.getCategoryById(transaction.categoryId)
            category?.let {
                val iconId = ResourcesUtil.getResourceIdByName(activityContext, it.icon)
                val drawable = ContextCompat.getDrawable(activityContext, iconId)

                withContext(Dispatchers.Main) {
                    categoryText?.text = it.name
                    categoryImage?.setImageDrawable(drawable)
                }
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay.${selectedMonth + 1}.$selectedYear"
            dateInput?.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
        datePickerDialog.show()
    }

    private fun isInputValid(amountInput: EditText?): Boolean {
        return amountInput?.text.toString().isNotEmpty() &&
                amountInput?.text.toString().toDoubleOrNull() != null &&
                selectedCategoryId != 0L
    }
}
