package com.example.kotlinprojectpro.ui.charts

import android.R.color
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.getColorForName
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_charts_page.*


class ChartsPage : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_charts_page, container, false)
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesChart.description.isEnabled = false
        categoriesChart.centerText = generateCenterText("1771")
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
                categoriesChart.centerText = generateCenterText("1771\nWYDANE")
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
        incomeChart.axisRight.isEnabled = false;
        incomeChart.xAxis.isEnabled = false;
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
        expenseChart.xAxis.isEnabled = false;
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
        bubbleEntries.add(PieEntry(650.15F, "grocery"))
        bubbleEntries.add(PieEntry(500F, "hobbies"))
        bubbleEntries.add(PieEntry(320.43F, "taxes"))
        bubbleEntries.add(PieEntry(201.20F, "investmets"))
        bubbleEntries.add(PieEntry(150F, "other"))
        val ds1 = PieDataSet(bubbleEntries, "Spożywcze")
        categoriesChart.setEntryLabelTextSize(0f);
        ds1.valueTextSize = 25f
        ds1.valueTextColor = Color.WHITE

        ds1.colors = getColors()
        return PieData(ds1)
    }

    private fun getEntriesLineChart(): LineData {
        val bubbleEntries = ArrayList<Entry>()
        bubbleEntries.add(Entry(1F, 15F))
        bubbleEntries.add(Entry(2F, 10F))
        bubbleEntries.add(Entry(3F, 30F))
        bubbleEntries.add(Entry(5F, 5F))
        bubbleEntries.add(Entry(6F, 50F))
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
        bubbleEntries.add(Entry(1F, 98F))
        bubbleEntries.add(Entry(2F, 12F))
        bubbleEntries.add(Entry(3F, 34F))
        bubbleEntries.add(Entry(5F, 58F))
        bubbleEntries.add(Entry(6F, 53F))
        val ds1 = LineDataSet(bubbleEntries, "Income")
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