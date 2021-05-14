package com.example.kotlinprojectpro

import android.content.Context
import androidx.core.content.ContextCompat

fun getColorForName(context: Context, color_name: String): Int{
    when(color_name) {
        "grocery" -> return ContextCompat.getColor(context, R.color.grocery)
        "hobbies" -> return ContextCompat.getColor(context, R.color.hobbies)
        "taxes" -> return  ContextCompat.getColor(context, R.color.taxes)
        "investments" -> return ContextCompat.getColor(context, R.color.investments)
        "other" -> return ContextCompat.getColor(context, R.color.other)
    }
return 0
}

