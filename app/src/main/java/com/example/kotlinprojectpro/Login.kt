package com.example.kotlinprojectpro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loginUser.setOnClickListener {
            val email = findViewById<EditText>(R.id.emailEditText).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
            FirebaseCommunicator.loginWithEmailAndPassword(email, password, this)
            val currentUser = FirebaseCommunicator.getCurrentlyLoggedUserUid()
            Log.i("USER", currentUser.toString())
            val myIntent = Intent(this, MainActivity::class.java)
            startActivity(myIntent)
        }
    }
}