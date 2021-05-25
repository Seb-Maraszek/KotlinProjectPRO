package com.example.kotlinprojectpro


import android.content.Context
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


object FirebaseCommunicator {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var mDatabase: DatabaseReference? = null

    init {

        mDatabase = FirebaseDatabase.getInstance().reference

    }

    fun registerWithEmailAndPassword(email: String, password: String, activity: Login){
        try{
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, OnCompleteListener {task->
                Toaster().registerToast(task.isSuccessful, activity)
            })}
        catch (e: Exception){
            Toaster().registerToast(false, activity)
    }
    }

    fun loginWithEmailAndPassword(email:String, password:String, activity:Login){
        try{
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, OnCompleteListener {task->
                Toaster().loginToast(task.isSuccessful, activity)
            })}
        catch (e: Exception){
            Toaster().loginToast(false, activity)
        }
    }
    fun getCurrentlyLoggedUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun logOutUser(){
        try{
            auth.signOut()}
        catch (e:Exception){
            Log.i("EXCEPTION_LOGOUT", e.toString())
        }
    }
}