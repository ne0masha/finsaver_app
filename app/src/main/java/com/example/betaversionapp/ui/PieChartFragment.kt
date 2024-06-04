package com.example.betaversionapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.betaversionapp.ApptViewModel
import com.example.betaversionapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PieChartFragment(
    private val viewModel: ApptViewModel
) : Fragment(R.layout.fragment_pie) {

    private lateinit var anyChartView: AnyChartView
    private lateinit var btnExpense: Button
    private lateinit var btnIncome: Button
    private lateinit var pieTitle: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        anyChartView = view.findViewById(R.id.pie_chart)
        pieTitle = view.findViewById(R.id.pie_title) // Инициализация TextView для заголовка

        val pie = AnyChart.pie()
        pie.background("#292929")

        btnExpense = view.findViewById(R.id.pie_button_expense)
        btnIncome = view.findViewById(R.id.pie_button_income)

        btnExpense.setOnClickListener {
            updatePieData(pie, false) // Показ расходов
        }

        btnIncome.setOnClickListener {
            updatePieData(pie, true) // Показ доходов
        }

        updatePieData(pie, false) // Показываем расходы по умолчанию
        anyChartView.setChart(pie)
        APIlib.getInstance().setActiveAnyChartView(anyChartView)
    }

    private fun updatePieData(pie: Pie, isIncomePie: Boolean) {
        lifecycleScope.launch {
            val dataEntries: MutableList<DataEntry> = withContext(Dispatchers.IO) {
                val entries: MutableList<DataEntry> = ArrayList()
                val idRange = if (isIncomePie) {
                    (1..3).map { it.toLong() } // id категорий доходов
                } else {
                    (4..14).map { it.toLong() } // id категорий расходов
                }
                idRange.forEach { id ->
                    val categoryName = viewModel.getCategoryById(id)?.name
                    val sum = viewModel.getSumByCategoryId(id)
                    if (sum != null) {
                        entries.add(ValueDataEntry(categoryName ?: "", sum))
                    }
                }
                entries
            }
            if (dataEntries.isEmpty()) {
                dataEntries.add(ValueDataEntry("", 0))
            }
            pie.data(dataEntries)

            // Обновление текста в зависимости от отображаемых данных
            val titleText = if (isIncomePie) "Ваши доходы" else "Ваши расходы"
            pieTitle.text = titleText
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        anyChartView.clear()
    }
}
