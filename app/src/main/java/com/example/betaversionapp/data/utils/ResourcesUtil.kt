package com.example.betaversionapp.data.utils

import android.content.Context

object ResourcesUtil {
    fun getResourceIdByName(context: Context, resourceName: String): Int {
        return context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    }
}
