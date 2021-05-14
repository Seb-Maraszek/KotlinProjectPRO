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
import com.example.kotlinprojectpro.getColorForName
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.main_fragment.*


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
        chart.centerText = generateCenterText("1771")
        chart.setCenterTextSize(16f)
        chart.holeRadius = 50f

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
        chart.data = getEntries()
        addNewExpense.setOnClickListener { onButtonShowPopupWindowClick(addNewExpense) }


        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
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
        chart.setEntryLabelTextSize(0f);
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