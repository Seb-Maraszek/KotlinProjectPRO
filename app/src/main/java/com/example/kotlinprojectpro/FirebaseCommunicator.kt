package com.example.kotlinprojectpro


import android.util.Log

import com.example.kotlinprojectpro.MainActivity.Companion.globalExpenseList
import com.example.kotlinprojectpro.models.Expense
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_budget_main.*


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
    fun getCurrentlyLoggedUserUid(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun addNewExpenseToDb(newExpense: Expense){
        Log.i("newExpense", newExpense.toString())
        mDatabase!!.child("expenses").child(getCurrentlyLoggedUserUid()).push().setValue(newExpense)
    }

    fun updateGlobalExpensesList() {
        return FirebaseDatabase.getInstance()
            .reference.child("expenses").child(getCurrentlyLoggedUserUid()).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        val expense_list = ArrayList<Any>()
                        for (ds in dataSnapshot.children) {
                            val expense: Expense? = ds.getValue(Expense::class.java)
                            if (expense != null) {
                                expense_list.add(expense)
                            }
                        }
                        globalExpenseList = expense_list
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }
}