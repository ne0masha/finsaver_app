package com.example.betaversionapp.ui


import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.betaversionapp.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

class PieChartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pie_chart)

        val pieChart = findViewById<PieChart>(R.id.pieChart)

        // Создание списка данных для графика
        val entries = mutableListOf<PieEntry>()
        entries.add(PieEntry(18.5f, "Green"))
        entries.add(PieEntry(26.7f, "Red"))
        entries.add(PieEntry(24.0f, "Blue"))
        entries.add(PieEntry(30.8f, "Yellow"))

        // Настройка набора данных
        val dataSet = PieDataSet(entries, "Colors")
        dataSet.colors = listOf(Color.GREEN, Color.RED, Color.BLUE, Color.YELLOW)

        // Создание объекта данных для графика и его применение
        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate() // Обновление графика
    }
}
