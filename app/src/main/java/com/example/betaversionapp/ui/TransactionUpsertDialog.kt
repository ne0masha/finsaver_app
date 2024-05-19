package com.example.betaversionapp.ui

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.Window
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

class TransactionUpsertDialog(context: Context,
  private var UpsertDialogListener: UpsertDialogListener,
  private var isIncome: Boolean
    ): AppCompatDialog(context)
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_transaction)

        val HeaderFragment = findViewById<TextView>(R.id.HeaderFragment)
        HeaderFragment?.text = if (isIncome) "ДОХОД" else "РАСХОД"

        val calendar = Calendar.getInstance()
        val initialYear = calendar.get(Calendar.YEAR)
        val initialMonth = calendar.get(Calendar.MONTH)
        val initialDay = calendar.get(Calendar.DAY_OF_MONTH)
        val initialDate = "$initialDay.${initialMonth + 1}.$initialYear"

        val amountInput = findViewById<EditText>(R.id.amount_input)
        val dateInput = findViewById<EditText>(R.id.date_input)
        val cancelButton = findViewById<Button>(R.id.cancel_button)
        val saveButton = findViewById<Button>(R.id.save_button)

        dateInput?.setText(initialDate)
        dateInput?.setOnClickListener { showDatePicker() }

        saveButton?.setOnClickListener {
            val date = DateConverter.dateToLong(dateInput?.text.toString())
            val amount: Long = if (amountInput?.text.toString().isNotBlank()) {
                (amountInput?.text.toString().toDouble() * 100).toLong()
            } else 0

            if (isInputValid(amountInput)) {
                val item = Transaction(isIncome, 1, date, amount)
                UpsertDialogListener.onAddButtonClicked(item)
                dismiss()
            } else {
                Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton?.setOnClickListener {
            cancel()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, { _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedDay.${selectedMonth + 1}.$selectedYear"
            findViewById<EditText>(R.id.date_input)?.setText(selectedDate)
        }, year, month, day)

        datePickerDialog.datePicker.maxDate = System.currentTimeMillis() // Ограничение выбора даты до текущей даты
        datePickerDialog.show()
    }

    private fun isInputValid(amountInput: EditText?): Boolean {
        return amountInput?.text.toString().isNotEmpty() && amountInput?.text.toString().toDoubleOrNull() != null
    }
}