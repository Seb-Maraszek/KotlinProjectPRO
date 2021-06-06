package com.example.kotlinprojectpro.ui.budget

import CategoryRecyclerAdapter
import android.app.DatePickerDialog
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
import android.widget.Button
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinprojectpro.*
import com.example.kotlinprojectpro.FirebaseCommunicator.updateGlobalExpensesList
import com.example.kotlinprojectpro.FirebaseCommunicator.updateGlobalIncomeList
import com.example.kotlinprojectpro.models.Category
import com.example.kotlinprojectpro.models.Income
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.fragment_budget_horizontal.*
import kotlinx.android.synthetic.main.fragment_budget_main.budgetCategoriesRecycler
import kotlinx.android.synthetic.main.fragment_budget_main.cardFirst
import java.util.*
import kotlin.collections.ArrayList

class BudgetHorizontal : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateGlobalIncomeList()
        updateGlobalExpensesList()
        return inflater.inflate(R.layout.fragment_budget_horizontal, container, false)
    }
    private var list = ArrayList<Category>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        expenseHorizontal.text = "$"+ getAllExpensesValue().toString()
        incomeTextHorizontal.text = "$"+ getAllIncomesValue().toString()
        percentageBudgetHorizontal.description.isEnabled = false
        percentageBudgetHorizontal.centerText = generateCenterText("$" +getAllExpensesValue().toString() + "\nOut of " + "$" + getAllIncomesValue().toString())
        percentageBudgetHorizontal.setCenterTextSize(16f)
        percentageBudgetHorizontal.holeRadius = 70f
        percentageBudgetHorizontal.setHoleColor(0)
        percentageBudgetHorizontal.setEntryLabelTextSize(0f)
        percentageBudgetHorizontal.animateY(1500, Easing.EasingOption.EaseOutBounce);
        percentageBudgetHorizontal.transparentCircleRadius = 70f
        if(getAllIncomesValue() != 0) {
            percentageBudgetHorizontal.data = getEntries()
            percentageBudgetHorizontal.data.setValueTextColor(0)
        }
        percentageBudgetHorizontal.legend.isEnabled = false
        list.add(Category(1000, "Food", 10))
        list.add(Category(1200, "Hobbies", 10))
        list.add(Category(1500, "Food", 10))
        list.add(Category(1000, "Food", 10))
        list.add(Category(1200, "Hobbies", 10))
        list.add(Category(1500, "Food", 10))
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
                if(income.text.toString() != "" || editDate.text.toString() != "") {
                    val newIncome = Income(
                        date,
                        income.text.toString()
                    )
                    FirebaseCommunicator.addNewIncomeToDb(newIncome)
                    MainActivity.globalIncomeList.add(newIncome)
                    incomeTextHorizontal.text = getAllIncomesValue().toString()
                    if(getAllIncomesValue() != 0) {
                        expenseHorizontal.text = "$"+getAllExpensesValue().toString()
                        incomeTextHorizontal.text = "$"+getAllIncomesValue().toString()
                        percentageBudgetHorizontal.centerText = generateCenterText("$" +getAllExpensesValue().toString() + "\nOut of " + "$" + getAllIncomesValue().toString())
                        percentageBudgetHorizontal.setCenterTextSize(16f)
                        percentageBudgetHorizontal.data = getEntries()
                        percentageBudgetHorizontal.data.setValueTextColor(0)
                        percentageBudgetHorizontal.invalidate()
                    }
                    popupWindow.dismiss()
                } else {
                    Toast.makeText(
                        context, "Failed to add expense",
                        Toast.LENGTH_SHORT
                    )
                }
            }
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