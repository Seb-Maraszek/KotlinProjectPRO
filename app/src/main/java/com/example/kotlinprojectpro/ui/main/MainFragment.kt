package com.example.kotlinprojectpro.ui.main

import android.app.DatePickerDialog
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.kotlinprojectpro.FirebaseCommunicator
import com.example.kotlinprojectpro.MainActivity.Companion.globalExpenseList
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.getAllExpensesValue
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.ui.home.HomePageFragment
import com.example.kotlinprojectpro.ui.budget.BudgetMain
import com.example.kotlinprojectpro.ui.charts.ChartsPage
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_set_up_account_spendings.*
import kotlinx.android.synthetic.main.fragment_budget_main.*
import kotlinx.android.synthetic.main.fragment_home_page.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.popup_create.*
import java.util.*


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
            val c: Calendar = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            var date = ""
            val editDate = contentView.findViewById<TextInputEditText>(R.id.editDate)
            fun addDate() {
                val dpd = DatePickerDialog(context!!, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val monthDate = monthOfYear + 1
                    date = "$dayOfMonth.$monthDate.$year"
                    editDate.setText(date)
                }, year, month, day)
                dpd.show()
            }
            fun addExpense(){
                //TODO DODAĆ TO BO JA NIE OGARNIAM TEJ NOTACJI
                var valueOfExpense = 2000
                val newExpense = Expense("Food",
                    "Burger",
                    "image",
                    date,
                    valueOfExpense.toString()
                )
                expense.text = "$"+getAllExpensesValue(valueOfExpense).toString()
                FirebaseCommunicator.addNewExpenseToDb(newExpense)
                globalExpenseList?.add(newExpense)
            }


            val submitButton = contentView.findViewById<Button>(R.id.button)
            submitButton.setOnClickListener{
                addExpense()
            }
            editDate.isFocusable = false
            editDate.setOnClickListener {
                addDate()
            }
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