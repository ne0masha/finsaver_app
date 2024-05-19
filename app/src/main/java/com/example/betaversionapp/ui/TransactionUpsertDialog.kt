package com.example.betaversionapp.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import com.example.betaversionapp.R
import com.example.betaversionapp.data.db.DateConverter
import com.example.betaversionapp.data.db.entities.Transaction
import java.util.Calendar

// TODO: переделать диалог так, чтобы он не только создавал, но и обновлял старые транзакции
// для этого можно добавить транзакцию как доп параметр (тр-я или null)

class TransactionUpsertDialog(
    context: Context,
    private var upsertDialogListener: UpsertDialogListener,
    private var transaction: Transaction?,
    private var isIncome: Boolean
): AppCompatDialog(context) {
    private var dateInput: EditText? = null
    private var amountInput: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_transaction)

        val headerFragment = findViewById<TextView>(R.id.HeaderFragment)
        headerFragment?.text = if (isIncome) "ДОХОД" else "РАСХОД"

        dateInput = findViewById(R.id.date_input)
        amountInput = findViewById(R.id.amount_input)
        val cancelButton = findViewById<Button>(R.id.cancel_button)
        val saveButton = findViewById<Button>(R.id.save_button)

        // Заполнение полей данными транзакции, если она передана
        transaction?.let { fillFieldsWithData(it) }

        if (transaction == null) {
            val calendar = Calendar.getInstance()
            val initialYear = calendar.get(Calendar.YEAR)
            val initialMonth = calendar.get(Calendar.MONTH)
            val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
            val initialDate = "$initialDay.${initialMonth + 1}.$initialYear"
            dateInput?.setText(initialDate)
        }



        amountInput?.addTextChangedListener(/* watcher = */ object : TextWatcher {
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
                        ) // удаляем последний символ, если условие выполняется
                    }
                }
            }
        })

        dateInput?.setOnClickListener { showDatePicker() }

        saveButton?.setOnClickListener {
            val date = DateConverter.dateToLong(dateInput?.text.toString())
            val amount: Long = if (amountInput?.text.toString().isNotBlank()) {
                (amountInput?.text.toString().toDouble() * 100).toLong()
            } else 0

            if (isInputValid(amountInput)) {
                if (transaction == null) {
                    // Создание новой транзакции
                    val item = Transaction(isIncome, 1, date, amount)
                    upsertDialogListener.onAddButtonClicked(item)
                } else {
                    // Обновление существующей транзакции
                    transaction?.let {
                        it.isIncome = isIncome
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
        return amountInput?.text.toString().isNotEmpty() && amountInput?.text.toString().toDoubleOrNull() != null
    }
}
