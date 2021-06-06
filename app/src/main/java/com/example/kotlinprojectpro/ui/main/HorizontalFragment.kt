package com.example.kotlinprojectpro.ui.main

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import com.example.kotlinprojectpro.*
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.ui.budget.BudgetHorizontal
import com.example.kotlinprojectpro.ui.charts.ChartsPage
import com.example.kotlinprojectpro.ui.home.HomeHorizontal
import com.example.kotlinprojectpro.ui.settings.SettingsHorizontal
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_budget_horizontal.*
import kotlinx.android.synthetic.main.fragment_budget_main.*
import kotlinx.android.synthetic.main.fragment_home_horizontal.*
import kotlinx.android.synthetic.main.fragment_home_page.*
import kotlinx.android.synthetic.main.fragment_horizontal.*
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*

class HorizontalFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        FirebaseCommunicator.updateGlobalExpensesList()
        FirebaseCommunicator.updateGlobalIncomeList()
        return inflater.inflate(R.layout.fragment_horizontal, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FirebaseCommunicator.getCurrentlyLoggedUserUid()?.let { it ->
            FirebaseDatabase.getInstance()
                .reference.child("expenses").child(it).addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val expense_list = ArrayList<Any>()
                            MainActivity.globalExpenseList = expense_list
                            for (ds in dataSnapshot.children) {
                                val expense: Expense? =
                                    ds.getValue(Expense::class.java)
                                if (expense != null) {
                                    MainActivity.globalExpenseList.add(expense)
                                }
                            }
                            MainActivity.globalExpenseList.sortByDescending { (it as Expense).date }
                            MainActivity.globalExpenseList
                            val expenses = getAllExpensesValue().toString()
                            MainActivity.globalExpenseList.remove(0)
                            MainActivity.globalExpenseList.add(0, expenses)
                            if(recyclerView != null){
                                (recyclerView.adapter as RecyclerViewAdapter).notifyDataSetChanged()
                                (recyclerView.adapter as RecyclerViewAdapter).updateAdapter(
                                    MainActivity.globalExpenseList
                                )
                            }
                            if(recyclerViewHorizontal != null) {
                                (recyclerViewHorizontal.adapter as RecyclerViewAdapter).notifyDataSetChanged()
                                (recyclerViewHorizontal.adapter as RecyclerViewAdapter).updateAdapter(
                                    MainActivity.globalExpenseList
                                )
                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
        FirebaseCommunicator.updateGlobalExpensesList()
        FirebaseCommunicator.updateGlobalIncomeList()
        addItemBtn.setOnClickListener {
            val contentView = LayoutInflater.from(context).inflate(
                R.layout.popup_create,
                null,
                false
            );
            val popupWindow = PopupWindow(
                contentView,
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            popupWindow.isOutsideTouchable = true
            popupWindow.isTouchable = true
            popupWindow.isFocusable = true;
            popupWindow.update()
            popupWindow.setOnDismissListener {  }
            val rootview: View = LayoutInflater.from(context).inflate(R.layout.main_fragment, null)
            popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0)
            val items = listOf(
                "Food",
                "IT",
                "Car",
                "Hobbies",
                "Investing",
                "Education",
                "Healthcare"
            )
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            val test = contentView.findViewById<AutoCompleteTextView>(R.id.categoryExpenseText)
            test.setAdapter(adapter)
            test.setText(items[0], false)
            val c: Calendar = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            var date = ""
            val editDate = contentView.findViewById<TextInputEditText>(R.id.editDate)
            fun addDate() {
                val dpd = DatePickerDialog(
                    context!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val monthDate = monthOfYear + 1
                        var convertedDay = "$dayOfMonth"
                        var convertedMonth = "$monthDate"

                        if(dayOfMonth < 10){
                            convertedDay = "0$dayOfMonth"
                        }
                        if(monthDate < 10){
                            convertedMonth = "0$monthDate"
                        }

                        date = convertedDay+"."+convertedMonth+".$year"
                        editDate.setText(date)
                    },
                    year,
                    month,
                    day
                )
                dpd.show()
            }
            fun addExpense(){
                val titleText = contentView.findViewById<TextInputEditText>(R.id.titleExpenseText)
                val categoryText = contentView.findViewById<AutoCompleteTextView>(R.id.categoryExpenseText)
                val expenseText = contentView.findViewById<TextInputEditText>(R.id.valueExpenseText)
                var image = ""
                val categoryTxt = categoryText.text.toString()
                if (categoryTxt == "Food") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.food
                } else if(categoryTxt == "IT") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.desktop
                } else if(categoryTxt == "Car") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.car
                } else if(categoryTxt == "Hobbies") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.artist
                } else if(categoryTxt == "Investing") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.investing
                } else if(categoryTxt == "Education") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.book
                } else if(categoryTxt == "Healthcare"){
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.healthcare
                }

                if(categoryText.text.toString() != "" || titleText.text.toString() != "" || expenseText.text.toString() != "") {
                    val newExpense = Expense(
                        categoryText.text.toString(),
                        titleText.text.toString(),
                        image,
                        date,
                        expenseText.text.toString()
                    )

                    FirebaseCommunicator.addNewExpenseToDb(newExpense)
                    MainActivity.globalExpenseList.add(newExpense)
                    val expenses = getAllExpensesValue().toString()
                    MainActivity.globalExpenseList.remove(0)
                    MainActivity.globalExpenseList.add(0, expenses)
                    if (recyclerView != null) {
                        (recyclerView.adapter as RecyclerViewAdapter).notifyDataSetChanged()
                        (recyclerView.adapter as RecyclerViewAdapter).updateAdapter(
                            MainActivity.globalExpenseList
                        )
                    }
                    if (recyclerViewHorizontal != null) {
                        (recyclerViewHorizontal.adapter as RecyclerViewAdapter).notifyDataSetChanged()
                        (recyclerViewHorizontal.adapter as RecyclerViewAdapter).updateAdapter(
                            MainActivity.globalExpenseList
                        )
                    }
                    if (percentageBudgetHorizontal != null) {
                        updateChart()
                        expenseHorizontal.text = "$" + getAllExpensesValue().toString()
                        incomeTextHorizontal.text = "$" + getAllIncomesValue().toString()
                    }

                } else {
                    Toast.makeText(
                        context, "Failed to add expense",
                        Toast.LENGTH_SHORT
                    )
                }
            }


            val submitButton = contentView.findViewById<Button>(R.id.button)
            submitButton.setOnClickListener{
                addExpense()
            }
            editDate.isFocusable = false
            editDate.setOnClickListener {
                addDate()
            }
        }

        if(savedInstanceState == null) {
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.horizontalPages, HomeHorizontal())?.commit()
            navigation_rail.setOnItemSelectedListener { item ->
                when(item.itemId) {
                    R.id.page_1_horizontal -> {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.horizontalPages, HomeHorizontal())?.commit()
                        return@setOnItemSelectedListener true
                    }
                    R.id.page_2_horizontal -> {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.horizontalPages, BudgetHorizontal())?.commit()
                        return@setOnItemSelectedListener true
                    }
                    R.id.page_3_horizontal -> {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.horizontalPages, ChartsPage())?.commit()
                        return@setOnItemSelectedListener true
                    }
                    else -> {
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(R.id.horizontalPages, SettingsHorizontal())?.commit()
                        return@setOnItemSelectedListener true
                    }
                }
                return@setOnItemSelectedListener false
            }
        }
    }

    private fun updateChart() {
        fun getColors(): ArrayList<Int> {
            val colors = ArrayList<Int>()
            colors.add(Color.rgb(255,193,7))
            colors.add(getColorForName(context!!, "primary"))
            return colors
        }
        fun getEntries(): PieData {
            val entries = ArrayList<PieEntry>()
            var percentage = getAllExpensesValue().toFloat() / getAllIncomesValue().toFloat() * 100
            if(percentage > 100) {
                percentage = 100F
            }
            entries.add(PieEntry(percentage))
            entries.add(PieEntry(100F - percentage))
            val dataSet = PieDataSet(entries, "Spent budget")
            dataSet.colors = getColors()
            return PieData(dataSet)
        }
        fun generateCenterText(text: String, primaryStringColor: String="#FFFFFFFF"): SpannableString? {
            val s = SpannableString(text)
            val len = text.substringBefore("\n").length
            s.setSpan(RelativeSizeSpan(1.8f), 0, len, 0)
            s.setSpan(ForegroundColorSpan(Color.parseColor(primaryStringColor)), 0, len, 0)
            s.setSpan(ForegroundColorSpan(Color.GRAY), len, s.length, 0)
            return s
        }
        percentageBudgetHorizontal.centerText =
            generateCenterText("$" + getAllExpensesValue().toString() + "\nOut of " + "$" + getAllIncomesValue().toString())
        percentageBudgetHorizontal.data = getEntries()
        percentageBudgetHorizontal.data.setValueTextColor(0)
        percentageBudgetHorizontal.invalidate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val transaction = fragmentManager?.beginTransaction()
        transaction?.detach(HomeHorizontal())
    }
}