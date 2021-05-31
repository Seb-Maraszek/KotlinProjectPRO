package com.example.kotlinprojectpro

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.ui.main.HorizontalFragment
import com.example.kotlinprojectpro.ui.main.MainFragment


class MainActivity : AppCompatActivity() {
    companion object {
        var globalExpenseList: ArrayList<Any>? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            changeFragment()
        }
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        changeFragment()
    }

    override fun onResume() {
        super.onResume()
        changeFragment()
    }
    private fun changeFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
            val verticalFragment = MainFragment()
            transaction.replace(R.id.container, verticalFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        } else {
            val horizontalFragment = HorizontalFragment()
            transaction.replace(R.id.container, horizontalFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}
