package com.example.kotlinprojectpro

import android.widget.Toast
import com.example.kotlinprojectpro.Login as Login

class Toaster {

    fun registerToast(isSuccessful: Boolean, activity: Login) {
        if(isSuccessful)
            Toast.makeText(activity, "Successfully registered", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(activity, "Failed to register", Toast.LENGTH_LONG).show()
    }

    fun loginToast(isSuccessful: Boolean, activity: Login){
        if(isSuccessful)
            Toast.makeText(activity, "Successfully logged in", Toast.LENGTH_LONG).show()
        else
            Toast.makeText(activity, "Failed to login", Toast.LENGTH_LONG).show()
    }
}