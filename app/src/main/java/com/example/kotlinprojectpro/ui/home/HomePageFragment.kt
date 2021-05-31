package com.example.kotlinprojectpro.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinprojectpro.FirebaseCommunicator.updateGlobalExpensesList
import com.example.kotlinprojectpro.MainActivity.Companion.globalExpenseList
import com.example.kotlinprojectpro.R
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.ui.main.MainViewModel
import com.example.kotlinprojectpro.ui.main.RecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_home_page.*

class HomePageFragment : Fragment() {
    var list = ArrayList<Any>()
    var dateList = ArrayList<String>()
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addThings()
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = RecyclerViewAdapter(list, dateList) {
            null
        }
        (recyclerView.adapter as RecyclerViewAdapter).notifyDataSetChanged()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private fun addThings(){
        updateGlobalExpensesList()
        Log.i("Things from DB", globalExpenseList.toString())
    }
}