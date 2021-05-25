package com.example.kotlinprojectpro.ui.budget

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
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.getColorForName
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_budget_main.*

class BudgetMain : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_budget_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        percentageBudget.description.isEnabled = false
        percentageBudget.centerText = generateCenterText("$3,400\nOut of 6,188")
        percentageBudget.setCenterTextSize(16f)
        percentageBudget.holeRadius = 70f
        percentageBudget.setHoleColor(0)
        percentageBudget.setEntryLabelTextSize(0f)
        percentageBudget.animateY(1500, Easing.EasingOption.EaseOutBounce);
        percentageBudget.transparentCircleRadius = 70f
        percentageBudget.data = getEntries()
        percentageBudget.data.setValueTextColor(0)
        percentageBudget.legend.isEnabled = false
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
        entries.add(PieEntry(82F))
        entries.add(PieEntry(18F))
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