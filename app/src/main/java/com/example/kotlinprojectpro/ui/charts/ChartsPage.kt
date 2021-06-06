package com.example.kotlinprojectpro.ui.charts

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kotlinprojectpro.*
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_charts_page.*


class ChartsPage : Fragment() {
    var lastWeekExpenses: ArrayList<Any> = ArrayList()
    var lastWeekIncomes: ArrayList<Any> = ArrayList()
    @SuppressLint("NewApi")
    var lastWeekDates = getLastWeekDates()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_charts_page, container, false)
    }


    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        for (i in 0..6){
            lastWeekExpenses.add(getLastWeekExpenses(i))
            lastWeekIncomes.add(getLastWeekIncome(i))
        }

        Log.i("LastWeekDates", lastWeekDates.toString())
        Log.i("LastWeekExpenses", lastWeekExpenses.toString())
        Log.i("LastWeekIncomes", lastWeekIncomes.toString())

        super.onViewCreated(view, savedInstanceState)
        categoriesChart.description.isEnabled = false
        val value = getAllExpensesValue()
        categoriesChart.centerText = generateCenterText(value.toString()+"\nSPENT")
        categoriesChart.setCenterTextSize(16f)
        categoriesChart.holeRadius = 60f
        categoriesChart.setHoleColor(0)
        categoriesChart.animateY(1500, Easing.EasingOption.EaseOutBack);
        categoriesChart.transparentCircleRadius = 60f
        categoriesChart.setEntryLabelTextSize(20f)
        categoriesChart.legend.isEnabled = false
        categoriesChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val pe = e as PieEntry
                categoriesChart.centerText = generateCenterText(pe.label)
            }

            override fun onNothingSelected() {
                val value = getAllExpensesValue()
                categoriesChart.centerText = generateCenterText(value.toString()+"\nSPENT")
            }
        })

        val l = categoriesChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        categoriesChart.data = getEntries()

        incomeChart.description.isEnabled = false
        incomeChart.setDrawGridBackground(false)
        incomeChart.setTouchEnabled(true)
        incomeChart.isDragEnabled = true
        incomeChart.setScaleEnabled(true)
        incomeChart.setPinchZoom(false)
        incomeChart.data = getEntriesLineChart()
        val yAxis = incomeChart.axisLeft;
        yAxis.textSize = 20f
        yAxis.textColor = Color.parseColor("#ffffff")
        yAxis.labelCount = 6
        yAxis.spaceTop = 20f
        yAxis.spaceBottom = 20f
        yAxis.xOffset = 25f
        val xxAxis = incomeChart.xAxis;
        xxAxis.textSize = 12f
        xxAxis.textColor = Color.parseColor("#ffffff")
        xxAxis.labelCount = 6
        xxAxis.valueFormatter =
            IAxisValueFormatter { value, axis -> lastWeekDates[value.toInt()].toString().substring(0,5) }
        xxAxis.position = XAxis.XAxisPosition.BOTTOM
        incomeChart.axisRight.isEnabled = false;
        incomeChart.xAxis.isEnabled = true;
        val lege: Legend = incomeChart.legend
        lege.isEnabled = false
        incomeChart.animateX(2000, Easing.EasingOption.EaseOutBack)

        expenseChart.description.isEnabled = false
        expenseChart.setDrawGridBackground(false)
        expenseChart.setTouchEnabled(true)
        expenseChart.isDragEnabled = true
        expenseChart.setScaleEnabled(true)
        expenseChart.setPinchZoom(false)
        expenseChart.data = getEntriesLineChartExpenses()

        val yAxisd = expenseChart.axisLeft;
        yAxisd.textSize = 20f
        yAxisd.spaceTop = 20f
        yAxisd.spaceBottom = 20f
        yAxisd.textColor = Color.parseColor("#ffffff")
        yAxisd.labelCount = 6
        yAxisd.xOffset = 25f

        expenseChart.axisRight.isEnabled = false;
        expenseChart.xAxis.isEnabled = true
        val xAxis = expenseChart.xAxis;
        xAxis.textSize = 12f
        xAxis.textColor = Color.parseColor("#ffffff")
        xAxis.labelCount = 6
        xAxis.valueFormatter =
            IAxisValueFormatter { value, axis -> lastWeekDates[value.toInt()].toString().substring(0,5) }
        expenseChart.extraBottomOffset = 20f
        incomeChart.extraBottomOffset = 20f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        val leged: Legend = expenseChart.legend
        leged.isEnabled = false
        expenseChart.animateX(2000, Easing.EasingOption.EaseOutBack)

    }

    private fun generateCenterText(text: String, primaryStringColor: String = "#FF03DAC5"): SpannableString? {
        val s = SpannableString(text)
        val len = text.substringBefore('\n').length
        s.setSpan(RelativeSizeSpan(1.8f), 0, len, 0)
        s.setSpan(ForegroundColorSpan(Color.parseColor(primaryStringColor)), 0, len, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), len, s.length, 0)
        return s
    }

    private fun getEntries(): PieData {
        val bubbleEntries = ArrayList<PieEntry>()
        for (item in MainActivity.allCategoriesNames){
            val expense = getExpensesForCategory(item)
            if (expense.value !=0){
                bubbleEntries.add(PieEntry(expense.value.toFloat(), expense.category))}
        }
        val ds1 = PieDataSet(bubbleEntries, "Spożywcze")
        categoriesChart.setEntryLabelTextSize(0f);
        ds1.valueTextSize = 25f
        ds1.valueTextColor = Color.WHITE

        ds1.colors = getColors()
        return PieData(ds1)
    }

    private fun getEntriesLineChart(): LineData {
        val bubbleEntries = ArrayList<Entry>()
        for (i in 0..6){
            var income = lastWeekIncomes[i] as Int
            bubbleEntries.add(Entry(i.toFloat(), income.toFloat()))
        }

        val ds1 = LineDataSet(bubbleEntries, "Income")
        categoriesChart.setEntryLabelTextSize(0f);
        ds1.valueTextSize = 20f
        ds1.valueTextColor = Color.WHITE
        ds1.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        ds1.setDrawFilled(true)
        ds1.setCircleColor(ContextCompat.getColor(context!!, R.color.teal_700))
        ds1.setCircleColorHole(ContextCompat.getColor(context!!, R.color.teal_700))
        ds1.fillColor = ContextCompat.getColor(context!!, R.color.primary)
        ds1.colors = getColors()
        return LineData(ds1)
    }

    private fun getEntriesLineChartExpenses(): LineData {

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

}