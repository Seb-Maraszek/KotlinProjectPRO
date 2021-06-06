package com.example.kotlinprojectpro.ui.main

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.example.kotlinprojectpro.*
import com.example.kotlinprojectpro.FirebaseCommunicator.updateGlobalExpensesList
import com.example.kotlinprojectpro.FirebaseCommunicator.updateGlobalIncomeList
import com.example.kotlinprojectpro.MainActivity.Companion.globalExpenseList
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.ui.budget.BudgetMain
import com.example.kotlinprojectpro.ui.charts.ChartsPage
import com.example.kotlinprojectpro.ui.home.HomePageFragment
import com.example.kotlinprojectpro.ui.settings.SettingsPage
import com.github.mikephil.charting.data.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_set_up_account_spendings.*
import kotlinx.android.synthetic.main.expense_item.*
import kotlinx.android.synthetic.main.expense_item.view.*
import kotlinx.android.synthetic.main.fragment_budget_horizontal.*
import kotlinx.android.synthetic.main.fragment_budget_main.*
import kotlinx.android.synthetic.main.fragment_charts_page.*
import kotlinx.android.synthetic.main.fragment_home_horizontal.*
import kotlinx.android.synthetic.main.fragment_home_page.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.popup_create.*
import java.util.*


class MainFragment : Fragment() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState == null) {
            bottomNavigationView.setOnNavigationItemSelectedListener(
                mOnNavigationItemSelectedListener
            )
            bottomNavigationView.menu[2].isEnabled = false

        }
        if(recyclerView != null) {
            recyclerView.adapter?.registerAdapterDataObserver(object : AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    recyclerView.adapter = RecyclerViewAdapter(globalExpenseList) {
                        null
                    }
                    (recyclerView.adapter as RecyclerViewAdapter).notifyDataSetChanged()
                }
            })
        }

        updateGlobalIncomeList()
        FirebaseCommunicator.getCurrentlyLoggedUserUid()?.let { it ->
            FirebaseDatabase.getInstance()
                .reference.child("expenses").child(it).addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val expense_list = ArrayList<Any>()
                            globalExpenseList = expense_list
                            for (ds in dataSnapshot.children) {
                                val expense: Expense? =
                                    ds.getValue(Expense::class.java)
                                if (expense != null) {
                                    globalExpenseList.add(expense)
                                }
                            }
                            globalExpenseList.sortByDescending { (it as Expense).date }
                            val expenses = getAllExpensesValue().toString()
                            globalExpenseList.remove(0)
                            globalExpenseList.add(0, expenses)
                            if(recyclerView != null){
                                (recyclerView.adapter as RecyclerViewAdapter).notifyDataSetChanged()
                                (recyclerView.adapter as RecyclerViewAdapter).updateAdapter(globalExpenseList)
                            }
                            if(recyclerViewHorizontal != null) {
                                (recyclerViewHorizontal.adapter as RecyclerViewAdapter).notifyDataSetChanged()
                                (recyclerViewHorizontal.adapter as RecyclerViewAdapter).updateAdapter(globalExpenseList)
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
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

            @RequiresApi(Build.VERSION_CODES.O)
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
                    globalExpenseList.add(newExpense)

                    if (recyclerView != null) {
                        (recyclerView.adapter as RecyclerViewAdapter).notifyDataSetChanged()
                        (recyclerView.adapter as RecyclerViewAdapter).updateAdapter(
                            globalExpenseList
                        )
                    }
                    if (recyclerViewHorizontal != null) {
                        (recyclerViewHorizontal.adapter as RecyclerViewAdapter).notifyDataSetChanged()
                        (recyclerViewHorizontal.adapter as RecyclerViewAdapter).updateAdapter(
                            globalExpenseList
                        )
                    }
                    if(percentageBudget != null){
                        expense.text = "$" + getAllExpensesValue().toString()
                        incomeText.text = "$" + getAllIncomesValue().toString()
                        updateChart()
                    }
                    if(expenseChart != null) {
                        expenseChart.data = getEntriesLineChartExpenses()
                        expenseChart.data.setValueTextColor(0)
                        expenseChart.invalidate()
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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getEntriesLineChartExpenses(): LineData {
        var lastWeekExpenses: ArrayList<Any> = ArrayList()
        for (i in 0..6){
            lastWeekExpenses.add(getLastWeekExpenses(i))
        }
        val bubbleEntries = ArrayList<Entry>()
        for (i in 0..6){
            var expense = lastWeekExpenses[i] as Int
            bubbleEntries.add(Entry(i.toFloat(), expense.toFloat()))
        }

        val ds1 = LineDataSet(bubbleEntries, "Expense")
        categoriesChart.setEntryLabelTextSize(0f);
        ds1.valueTextSize = 20f
        ds1.valueTextColor = Color.WHITE
        ds1.mode = LineDataSet.Mode.CUBIC_BEZIER
        ds1.setDrawFilled(true)
        ds1.setCircleColor(ContextCompat.getColor(context!!, R.color.primary))
        ds1.setCircleColorHole(ContextCompat.getColor(context!!, R.color.primary))
        ds1.fillColor = ContextCompat.getColor(context!!, R.color.teal_700)
        ds1.colors = getColors()
        return LineData(ds1)
    }

    private fun getColors(): ArrayList<Int> {
        val colors = ArrayList<Int>()
        colors.add(getColorForName(context!!, "grocery"))
        colors.add(getColorForName(context!!, "hobbies"))
        colors.add(getColorForName(context!!, "taxes"))
        colors.add(getColorForName(context!!, "other"))
        colors.add(getColorForName(context!!, "investments"))
        return colors
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
        percentageBudget.centerText = generateCenterText("$" +getAllExpensesValue().toString() + "\nOut of " + "$" + getAllIncomesValue().toString())
        percentageBudget.setCenterTextSize(16F)
        percentageBudget.data = getEntries()
        percentageBudget.data.setValueTextColor(0)
        percentageBudget.invalidate()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val transaction = fragmentManager?.beginTransaction()
        transaction?.detach(HomePageFragment())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        if(savedInstanceState == null) {
            updateGlobalExpensesList()
            updateGlobalIncomeList()
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            val verticalFragment = HomePageFragment()
            transaction?.replace(R.id.fragmentsContainer, verticalFragment)
            transaction?.addToBackStack(null)
            transaction?.commit()
        }

        return inflater.inflate(R.layout.main_fragment, container, false)
    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.page_1 -> {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentsContainer, HomePageFragment())?.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.page_2 -> {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentsContainer, BudgetMain())?.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.page_3 -> {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentsContainer, ChartsPage())?.commit()
                return@OnNavigationItemSelectedListener true
            }
            R.id.page_4 -> {
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragmentsContainer, SettingsPage())?.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}