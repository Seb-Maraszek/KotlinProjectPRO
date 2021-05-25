package com.example.kotlinprojectpro.ui.charts

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.getColorForName
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoriesChart.description.isEnabled = false
        categoriesChart.centerText = generateCenterText("1771")
        categoriesChart.setCenterTextSize(16f)
        categoriesChart.holeRadius = 50f
        categoriesChart.setHoleColor(0)
        categoriesChart.animateY(2000);
        categoriesChart.transparentCircleRadius = 50f
        categoriesChart.setEntryLabelTextSize(20f)

        categoriesChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
                val pe = e as PieEntry
                categoriesChart.centerText = generateCenterText(pe.label+"\nKWIECIEŃ")
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
    }

    private fun generateCenterText(text: String, primaryStringColor: String="#FF03DAC5"): SpannableString? {
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