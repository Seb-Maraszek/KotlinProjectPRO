package com.example.kotlinprojectpro

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.example.kotlinprojectpro.MainActivity.Companion.globalExpenseList
import com.example.kotlinprojectpro.MainActivity.Companion.globalIncomeList
import com.example.kotlinprojectpro.models.Category
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.models.Income
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList

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
    for (item in globalExpenseList) {
        if(item is Expense){
            totalExpenses += try{
                item.value.toInt()
            } catch(e: Exception){
                0
            }
        }
    }
    return totalExpenses+additional
}

fun getAllIncomesValue(additional: Int=0): Int {
    var totalIncomes = 0
    for (item in globalIncomeList) {
        if(item is Income){
            totalIncomes += try{
                item.value.toInt()
            } catch(e: Exception){
                0
            }
        }
    }
    return totalIncomes+additional
}

fun getExpensesForCategory(name: String): Category {
    var sum = 0
    for(i in globalExpenseList){
        if(i is Expense && i.category == name){
            sum += try{
                i.value.toInt()
            } catch (e: java.lang.Exception){
                0
            }
        }
    }

    var progression = try {
        100*sum/ getAllExpensesValue()
    }
    catch (e: Exception){
        0
    }

    return Category(sum, name, progression)
}

fun getExpensesByCategory(): ArrayList<Category> {
    val list = ArrayList<Category>()
    for (item in MainActivity.allCategoriesNames){
        list.add(getExpensesForCategory(item))
    }
    return list
}

@RequiresApi(Build.VERSION_CODES.O)
fun getLastWeekDates(): ArrayList<Any> {
    var days: ArrayList<Any> = ArrayList()
    for (i in 0..6){
        var day = LocalDateTime.now().minus(i.toLong(), ChronoUnit.DAYS)
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        val formatted = day.format(formatter)
        days.add(formatted)
    }
    return days
}

@RequiresApi(Build.VERSION_CODES.O)
fun getLastWeekExpenses(minus_days:Int): Int {
    var day = getLastWeekDates()[minus_days]
    var sum = 0
    for (i in globalExpenseList){
        if(i is Expense){
            if(i.date == day.toString()){
                sum += try {
                    i.value.toInt()
                }
                catch (e: java.lang.Exception){
                    0
                }
            }
        }
    }
    Log.i("SUM_expense", sum.toString())
    return sum
}

@RequiresApi(Build.VERSION_CODES.O)
fun getLastWeekIncome(minus_days:Int): Int {
    var day = getLastWeekDates()[minus_days]
    var sum = 0
    for (i in globalIncomeList){
        if(i is Income){
            if(i.date == day.toString()){
                sum += try {
                    i.value.toInt()
                }
                catch (e: java.lang.Exception){
                    0
                }
            }
        }
    }
    Log.i("SUM_income", sum.toString())
    return sum
}

