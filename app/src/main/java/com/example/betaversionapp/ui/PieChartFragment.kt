package com.example.betaversionapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.betaversionapp.R

class PieChartFragment: Fragment(R.layout.fragment_pie) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: написать загрузку данных в круговую диаграмму!
        // getTransactionsByCategory() уже написана, надо применить
        // и продумать, что будет отображаться без операций! Какую-то пустую серую диаграмму
    }
}