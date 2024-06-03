package com.example.betaversionapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.map
import com.anychart.APIlib
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.example.betaversionapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PieChartFragment(
    private val viewModel: TransactionsListViewModel
) : Fragment(R.layout.fragment_pie){

    private lateinit var anyChartView: AnyChartView
    private lateinit var btn: TextView
    private val pie = AnyChart.pie()
    private var isIncomePie = false

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        anyChartView = view.findViewById(R.id.pie_chart)

        pie.background("#292929")
        anyChartView.setChart(pie)
        APIlib.getInstance().setActiveAnyChartView(anyChartView)
        updatePieData(isIncomePie, pie)

        btn = view.findViewById(R.id.pie_chart_title)
        btn.setOnClickListener {
            isIncomePie = !isIncomePie
            updatePieData(isIncomePie, pie)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        anyChartView.clear()
    }

    private fun updatePieData(isIncomePie: Boolean, pie: Pie) {
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
                        entries.add(ValueDataEntry(categoryName.toString(), sum))
                    }
                }
                entries
            }
            if (dataEntries.isEmpty()) {
                dataEntries.add(ValueDataEntry("", 0))
            }
            pie.data(dataEntries)
        }
    }
}
