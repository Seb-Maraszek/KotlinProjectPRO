package com.example.kotlinprojectpro.ui.main

import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.example.kotlinprojectpro.FirebaseCommunicator
import com.example.kotlinprojectpro.FirebaseCommunicator.updateGlobalExpensesList
import com.example.kotlinprojectpro.MainActivity.Companion.globalExpenseList
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.getAllExpensesValue
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.ui.budget.BudgetMain
import com.example.kotlinprojectpro.ui.charts.ChartsPage
import com.example.kotlinprojectpro.ui.home.HomePageFragment
import com.example.kotlinprojectpro.ui.settings.SettingsPage
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_set_up_account_spendings.*
import kotlinx.android.synthetic.main.expense_item.*
import kotlinx.android.synthetic.main.expense_item.view.*
import kotlinx.android.synthetic.main.fragment_budget_main.*
import kotlinx.android.synthetic.main.fragment_home_horizontal.*
import kotlinx.android.synthetic.main.fragment_home_page.*
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.popup_create.*
import java.util.*


class MainFragment : Fragment() {

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(recyclerView != null){
            (recyclerView.adapter as RecyclerViewAdapter).notifyDataSetChanged()
        }
        if(recyclerViewHorizontal != null) {
            (recyclerViewHorizontal.adapter as RecyclerViewAdapter).notifyDataSetChanged()
        }
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
            val contentView = LayoutInflater.from(context).inflate(
                R.layout.popup_create,
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
            val items = listOf(
                "Food",
                "IT",
                "Car",
                "Hobbies",
                "Investing",
                "Education",
                "Healthcare"
            )
            val adapter = ArrayAdapter(requireContext(), R.layout.list_item, items)
            val test = contentView.findViewById<AutoCompleteTextView>(R.id.categoryExpenseText)
            test.setAdapter(adapter)
            test.setText(items[0], false)
            val c: Calendar = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            var date = ""
            val editDate = contentView.findViewById<TextInputEditText>(R.id.editDate)
            fun addDate() {
                val dpd = DatePickerDialog(
                    context!!,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        val monthDate = monthOfYear + 1
                        date = "$dayOfMonth.$monthDate.$year"
                        editDate.setText(date)
                    },
                    year,
                    month,
                    day
                )
                dpd.show()
            }
            fun addExpense(){
                val titleText = contentView.findViewById<TextInputEditText>(R.id.titleExpenseText)
                val categoryText = contentView.findViewById<AutoCompleteTextView>(R.id.categoryExpenseText)
                val expenseText = contentView.findViewById<TextInputEditText>(R.id.valueExpenseText)
                var image = ""
                val categoryTxt = categoryText.text.toString()
                if (categoryTxt == "Food") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.food
                } else if(categoryTxt == "IT") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.desktop
                } else if(categoryTxt == "Car") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.car
                } else if(categoryTxt == "Hobbies") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.artist
                } else if(categoryTxt == "Investing") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.investing
                } else if(categoryTxt == "Education") {
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.book
                } else if(categoryTxt == "Healthcare"){
                    image = "android.resource://com.example.kotlinprojectpro/" + R.drawable.healthcare
                }

                val newExpense = Expense(
                    categoryText.text.toString(),
                    titleText.text.toString(),
                    image,
                    date,
                    expenseText.text.toString()
                )
                FirebaseCommunicator.addNewExpenseToDb(newExpense)
                globalExpenseList.add(newExpense)
                (recyclerView.adapter as RecyclerViewAdapter).notifyDataSetChanged()
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
            updateGlobalExpensesList()
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
                    ?.replace(R.id.fragmentsContainer, SettingsPage())?.commit()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}