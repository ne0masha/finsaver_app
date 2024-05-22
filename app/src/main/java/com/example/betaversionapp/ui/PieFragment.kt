package com.example.betaversionapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.betaversionapp.R

class PieFragment: Fragment(R.layout.fragment_pie) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Pie", "Pie was loaded!")
    }
}