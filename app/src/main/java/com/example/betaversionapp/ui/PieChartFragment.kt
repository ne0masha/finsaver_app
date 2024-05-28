package com.example.yourapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.charts.Pie
import com.anychart.data.View
import com.example.betaversionapp.R

class PieChartFragment : Fragment(R.layout.fragment_pie){

    private lateinit var anyChartView: AnyChartView
    private val months = arrayOf("January", "February", "March", "April")
    private val salary = intArrayOf(16000, 20000, 30000, 50000)

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        anyChartView = view.findViewById(R.id.pie_chart)
        setupChartView()
    }

    private fun setupChartView() {
        val pie = AnyChart.pie()
        val dataEntries: MutableList<DataEntry> = ArrayList()

        for (i in months.indices) {
            dataEntries.add(ValueDataEntry(months[i], salary[i]))
        }

        pie.data(dataEntries)
        pie.title("Salary")
        anyChartView.setChart(pie)
    }
}
