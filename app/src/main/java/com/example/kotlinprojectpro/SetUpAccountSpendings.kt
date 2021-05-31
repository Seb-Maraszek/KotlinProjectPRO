package com.example.kotlinprojectpro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SetUpAccountSpendings : AppCompatActivity() {
    var mDatabase: DatabaseReference? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mDatabase = FirebaseDatabase.getInstance().reference
        setContentView(R.layout.activity_set_up_account_spendings)
    }
        fun registerUser(view: View) {
            val billingTime = findViewById<TextView>(R.id.defaultTime).text.toString().toInt()
            val initialMoney = findViewById<TextView>(R.id.initialMoney).text.toString().toFloat()
            val currency = findViewById<TextView>(R.id.currencyInput).text.toString()
            val predictedIncome = findViewById<TextView>(R.id.predictedIncome).text.toString().toFloat()
            val predictedOutcome = findViewById<TextView>(R.id.predictedOutcome).text.toString().toFloat()
            val user_Id = FirebaseAuth.getInstance().currentUser?.uid

            val userData = UserData(
                currency = currency as String?,
                currentMoney = initialMoney as Float?,
                billingPeriodDays = billingTime as Int?,
                totalPredictedIncome = predictedIncome as Float?,
                totalPredictedOutcome = predictedOutcome as Float?,
            )
            mDatabase!!.child("user_data").child(user_Id.toString()).setValue(userData)
        }

}