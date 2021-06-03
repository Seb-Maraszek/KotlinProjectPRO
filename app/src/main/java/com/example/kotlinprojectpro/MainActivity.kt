package com.example.kotlinprojectpro

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.example.kotlinprojectpro.ui.main.HorizontalFragment
import com.example.kotlinprojectpro.ui.main.MainFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    companion object {
        var globalExpenseList: ArrayList<Any> = ArrayList()
        var globalIncomeList: ArrayList<Any> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        auth = Firebase.auth
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

    public override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            startActivity(Intent(this, Login::class.java))
            finish()
            return
        }
    }

}
