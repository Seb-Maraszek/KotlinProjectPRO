package com.example.kotlinprojectpro.ui.budget

import CategoryRecyclerAdapter
import android.app.DatePickerDialog
import android.app.Dialog
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
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinprojectpro.*
import com.example.kotlinprojectpro.FirebaseCommunicator.addNewIncomeToDb
import com.example.kotlinprojectpro.FirebaseCommunicator.updateGlobalExpensesList
import com.example.kotlinprojectpro.FirebaseCommunicator.updateGlobalIncomeList
import com.example.kotlinprojectpro.MainActivity.Companion.globalIncomeList
import com.example.kotlinprojectpro.models.Category
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.models.Income
import com.example.kotlinprojectpro.ui.main.RecyclerViewAdapter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_budget_main.*
import kotlinx.android.synthetic.main.fragment_home_horizontal.*
import kotlinx.android.synthetic.main.fragment_home_page.*
import java.util.*
import kotlin.collections.ArrayList

class BudgetMain : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateGlobalIncomeList()
        updateGlobalExpensesList()
        return inflater.inflate(R.layout.fragment_budget_main, container, false)
    }
    private var list = ArrayList<Category>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        percentageBudget.description.isEnabled = false
        expense.text = "$"+getAllExpensesValue().toString()
        incomeText.text = "$"+getAllIncomesValue().toString()
        percentageBudget.centerText = generateCenterText("$" +getAllExpensesValue().toString() + "\nOut of " + "$" + getAllIncomesValue().toString())
        percentageBudget.setCenterTextSize(16f)
        percentageBudget.holeRadius = 70f
        percentageBudget.setHoleColor(0)
        percentageBudget.setEntryLabelTextSize(0f)
        percentageBudget.animateY(1500, Easing.EasingOption.EaseOutBounce);
        percentageBudget.transparentCircleRadius = 70f
        if(getAllIncomesValue() != 0) {
            percentageBudget.data = getEntries()
            percentageBudget.data.setValueTextColor(0)
        }
        percentageBudget.legend.isEnabled = false
        list.add(Category(1000, "Food"))
        list.add(Category(1200, "Hobbies"))
        list.add(Category(1500, "Food"))
        list.add(Category(1000, "Food"))
        list.add(Category(1200, "Hobbies"))
        list.add(Category(1500, "Food"))
        budgetCategoriesRecycler.layoutManager = LinearLayoutManager(context)
        budgetCategoriesRecycler.adapter = CategoryRecyclerAdapter(list) {
            null
        }
        (budgetCategoriesRecycler.adapter as CategoryRecyclerAdapter).notifyDataSetChanged()

        cardFirst.setOnClickListener {
            val contentView = LayoutInflater.from(context).inflate(
                R.layout.popup_create_income,
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
            val c: Calendar = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            var date = ""
            val editDate = contentView.findViewById<TextInputEditText>(R.id.editDate)
            fun addIncome(){
                val income = contentView.findViewById<TextInputEditText>(R.id.valueIncomeText)
                val newIncome = Income(
                    date,
                    income.text.toString()
                )
                addNewIncomeToDb(newIncome)
                globalIncomeList.add(newIncome)
                incomeText.text = getAllIncomesValue().toString()
            }
            fun addDate() {
                val dpd = DatePickerDialog(
                    context!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val monthDate = monthOfYear + 1
                        date = "$dayOfMonth.$monthDate.$year"
                        editDate.setText(date)
                    },
                    year,
                    month,
                    day
                )
                dpd.show()
            }
            val submitButton = contentView.findViewById<Button>(R.id.button)
            submitButton.setOnClickListener{
                addIncome()
            }
            editDate.isFocusable = false
            editDate.setOnClickListener {
                addDate()
            }
        }
    }

    private fun generateCenterText(text: String, primaryStringColor: String="#FFFFFFFF"): SpannableString? {
        val s = SpannableString(text)
        val len = text.substringBefore("\n").length
        s.setSpan(RelativeSizeSpan(1.8f), 0, len, 0)
        s.setSpan(ForegroundColorSpan(Color.parseColor(primaryStringColor)), 0, len, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), len, s.length, 0)
        return s
    }

    private fun getEntries(): PieData {
        val entries = ArrayList<PieEntry>()
        val percentage = getAllExpensesValue().toFloat() / getAllIncomesValue().toFloat() * 100
        println( percentage)
        entries.add(PieEntry(percentage))
        entries.add(PieEntry(100F - percentage))
        val dataSet = PieDataSet(entries, "Spent budget")
        dataSet.colors = getColors()
        return PieData(dataSet)
    }

    private fun getColors(): ArrayList<Int> {
        val colors = ArrayList<Int>()
        colors.add(Color.rgb(255,193,7))
        colors.add(getColorForName(context!!, "primary"))
        return colors
    }



}