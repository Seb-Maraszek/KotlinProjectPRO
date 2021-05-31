package com.example.kotlinprojectpro.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.ui.main.RecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_home_horizontal.*

class HomeHorizontal : Fragment() {
    var list = ArrayList<Any>()
    var dateList = ArrayList<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addMock()
        recyclerViewHorizontal.layoutManager = LinearLayoutManager(context)
        recyclerViewHorizontal.adapter = RecyclerViewAdapter(list, dateList) {
            null
        }
        (recyclerViewHorizontal.adapter as RecyclerViewAdapter).notifyDataSetChanged()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home_horizontal, container, false)
    }

    private fun addMock(){
        list.add("$123,541.00")
        dateList.add("")
        val expense = Expense("Food", "Burger", "android.resource://com.example.kotlinprojectpro/" + R.drawable.food, "Today", "180$")
        list.add(expense)
        dateList.add(expense.date)
        list.add(expense)
        dateList.add(expense.date)
        val expenseDesktop = Expense("Software", "Spotify", "android.resource://com.example.kotlinprojectpro/" + R.drawable.desktop, "Today", "9.67$")
        list.add(expenseDesktop)
        dateList.add(expenseDesktop.date)
        val expenseHobbies = Expense("Hobbies", "Football", "android.resource://com.example.kotlinprojectpro/" + R.drawable.artist, "Yesterday", "2021$")
        list.add(expenseHobbies)
        dateList.add(expenseHobbies.date)
        list.add(expenseHobbies)
        dateList.add(expenseHobbies.date)
        list.add(expenseHobbies)
        dateList.add(expenseHobbies.date)
        list.add(expenseHobbies)
        dateList.add(expenseHobbies.date)
        list.add(expenseHobbies)
        dateList.add(expenseHobbies.date)
    }
}