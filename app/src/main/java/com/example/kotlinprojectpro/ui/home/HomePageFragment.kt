package com.example.kotlinprojectpro.ui.home

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.getColorForName
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.ui.main.MainViewModel
import com.example.kotlinprojectpro.ui.main.RecyclerViewAdapter
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_home_page.*

class HomePageFragment : Fragment() {
    private lateinit var dialog : Dialog
    var list = ArrayList<Any>()
    var dateList = ArrayList<String>()
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        /*chart.description.isEnabled = false
        chart.centerText = generateCenterText("1771")
        chart.setCenterTextSize(16f)
        chart.holeRadius = 50f
        bottomNavigationView.menu.getItem(2).isEnabled = false
        chart.setHoleColor(0)
        chart.legend.isEnabled = false
        chart.animateY(2000);
        chart.transparentCircleRadius = 50f
        chart.setEntryLabelTextSize(20f)

        chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val pe = e as PieEntry
                chart.centerText = generateCenterText(pe.label+"\nKWIECIEŃ")
            }

            override fun onNothingSelected() {
                chart.centerText = generateCenterText("1771\nWYDANE")
            }
        })

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        dialog = context?.let { Dialog(it) }!!
        chart.data = getEntries()*/

        addMock()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = RecyclerViewAdapter(list, dateList) {
            null
        }
        (recyclerView.adapter as RecyclerViewAdapter).notifyDataSetChanged()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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