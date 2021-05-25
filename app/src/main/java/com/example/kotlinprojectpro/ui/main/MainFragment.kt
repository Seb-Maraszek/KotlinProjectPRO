package com.example.kotlinprojectpro.ui.main

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.ui.home.HomePageFragment
import com.example.kotlinprojectpro.ui.budget.BudgetMain
import com.example.kotlinprojectpro.ui.charts.ChartsPage
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.popup_create.*


class MainFragment : Fragment() {
    companion object {
        fun newInstance() = MainFragment()
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState == null) {
            bottomNavigationView.setOnNavigationItemSelectedListener(
                mOnNavigationItemSelectedListener
            )
            bottomNavigationView.menu[2].isEnabled = false
        }
        addItemBtn.setOnClickListener {
            val contentView = LayoutInflater.from(context).inflate(R.layout.popup_create, null, false);
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
            val items = listOf("Food", "IT", "Car", "Hobbies", "Investing", "Education", "Healthcare")
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            val test = contentView.findViewById<AutoCompleteTextView>(R.id.textfield)
            test.setAdapter(adapter)
            test.setText(items[0],false)

        }
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
                    ?.replace(R.id.fragmentsContainer, BudgetMain())?.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}