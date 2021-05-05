package com.example.kotlinprojectpro.ui.main

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kotlinprojectpro.R
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.popup_create.*


class MainFragment : Fragment() {
    private lateinit var dialog : Dialog
    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chart.description.isEnabled = false
        chart.centerText = generateCenterText()
        chart.setCenterTextSize(16f)
        chart.holeRadius = 50f
        chart.setHoleColor(0)
        chart.legend.isEnabled = false
        chart.transparentCircleRadius = 50f
        chart.setEntryLabelTextSize(20f)
        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        dialog = context?.let { Dialog(it) }!!
        chart.data = getEntries()
        addNewExpense.setOnClickListener { onButtonShowPopupWindowClick(addNewExpense) }


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun generateCenterText(): SpannableString? {
        val s = SpannableString("Wydatki\ndzisiejsze")
        s.setSpan(RelativeSizeSpan(2f), 0, 8, 0)
        s.setSpan(ForegroundColorSpan(Color.parseColor("#FF03DAC5")), 0, 8, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 8, s.length, 0)
        return s
    }

    private fun getEntries(): PieData {
        val bubbleEntries = ArrayList<PieEntry>()
        bubbleEntries.add(PieEntry(20F))
        bubbleEntries.add(PieEntry(50F))
        bubbleEntries.add(PieEntry(30F))
        bubbleEntries.add(PieEntry(35.5F))
        bubbleEntries.add(PieEntry(60F))
        val ds1 = PieDataSet(bubbleEntries, "Spożywcze")
        ds1.valueTextSize = 20f
        ds1.colors = getColors()
        return PieData(ds1)
    }

    private fun getColors(): ArrayList<Int> {
        val colors = ArrayList<Int>()
        colors.add(Color.GREEN)
        colors.add(Color.BLUE)
        colors.add(Color.RED)
        colors.add(Color.MAGENTA)
        colors.add(Color.YELLOW)
        return colors
    }

    private fun onButtonShowPopupWindowClick(view: View?) {
        val contentView = LayoutInflater.from(context).inflate(R.layout.popup_create, null, false);
        val popupWindow = PopupWindow(
            contentView,
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        );
        popupWindow.isOutsideTouchable = true
        popupWindow.isTouchable = true
        popupWindow.setOnDismissListener {  }
        val rootview: View = LayoutInflater.from(context).inflate(R.layout.main_fragment, null)
        popupWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0)


    }

}