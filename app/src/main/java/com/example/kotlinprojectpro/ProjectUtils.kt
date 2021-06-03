package com.example.kotlinprojectpro

import android.content.Context
import androidx.core.content.ContextCompat
import com.example.kotlinprojectpro.MainActivity.Companion.globalExpenseList
import com.example.kotlinprojectpro.MainActivity.Companion.globalIncomeList
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.models.Income

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

fun getAllExpensesValue(additional: Int=0): Int {
    var totalExpenses = 0
    if(globalExpenseList != null){
    for (item in globalExpenseList!!) {
        if(item is Expense){
            totalExpenses += item.value.toInt()
        }
    }}
    else{
        return additional
    }
    return totalExpenses+additional
}

fun getAllIncomesValue(additional: Int=0): Int {
    var totalIncomes = 0
    if(globalIncomeList != null){
        for (item in globalIncomeList!!) {
            if(item is Income){
                totalIncomes += item.value.toInt()
            }
        }}
    else{
        return additional
    }
    return totalIncomes+additional
}