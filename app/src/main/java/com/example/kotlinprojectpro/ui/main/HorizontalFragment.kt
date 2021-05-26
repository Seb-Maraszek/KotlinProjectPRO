package com.example.kotlinprojectpro.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.view.get
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.ui.budget.BudgetHorizontal
import com.example.kotlinprojectpro.ui.budget.BudgetMain
import com.example.kotlinprojectpro.ui.charts.ChartsPage
import com.example.kotlinprojectpro.ui.home.HomeHorizontal
import com.example.kotlinprojectpro.ui.home.HomePageFragment
import com.example.kotlinprojectpro.ui.settings.SettingsHorizontal
import com.example.kotlinprojectpro.ui.settings.SettingsPage
import kotlinx.android.synthetic.main.fragment_horizontal.*
import kotlinx.android.synthetic.main.main_fragment.*

class HorizontalFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_horizontal, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val transaction = fragmentManager?.beginTransaction()
        transaction?.detach(HomeHorizontal())
    }
}