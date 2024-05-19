package com.example.betaversionapp.data.db


import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    companion object {
        @JvmStatic
        fun dateToLong(dateString: String): Long {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val date = dateFormat.parse(dateString)
            return date?.time ?: 0L
        }

        @JvmStatic
        fun longToDate(longValue: Long): String {
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            val date = Date(longValue)
            return dateFormat.format(date)
        }
    }
}