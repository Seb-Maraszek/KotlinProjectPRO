package com.example.kotlinprojectpro.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinprojectpro.FirebaseCommunicator
import com.example.kotlinprojectpro.FirebaseCommunicator.updateGlobalExpensesList
import com.example.kotlinprojectpro.MainActivity.Companion.globalExpenseList
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.ui.main.RecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_home_horizontal.*
import kotlinx.android.synthetic.main.fragment_home_horizontal.textView4
import kotlinx.android.synthetic.main.fragment_home_page.*

class HomeHorizontal : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerViewHorizontal.layoutManager = LinearLayoutManager(context)
        val textField = textView4
        textField.text = FirebaseCommunicator.getCurrentlyLoggedInUser()
        recyclerViewHorizontal.adapter = RecyclerViewAdapter(globalExpenseList) {
            null
        }

        (recyclerViewHorizontal.adapter as RecyclerViewAdapter).notifyDataSetChanged()
        (recyclerViewHorizontal.adapter as RecyclerViewAdapter).updateAdapter(globalExpenseList)

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        updateGlobalExpensesList()
        return inflater.inflate(R.layout.fragment_home_horizontal, container, false)
    }
}