package com.example.kotlinprojectpro

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.example.kotlinprojectpro.ui.main.HorizontalFragment
import com.example.kotlinprojectpro.ui.main.MainFragment
import kotlinx.android.synthetic.main.main_fragment.*
import kotlinx.android.synthetic.main.popup_create.*

class MainActivity : AppCompatActivity() {

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
