package com.example.kotlinprojectpro


import android.util.Log
import com.example.kotlinprojectpro.MainActivity.Companion.globalExpenseList
import com.example.kotlinprojectpro.MainActivity.Companion.globalIncomeList
import com.example.kotlinprojectpro.models.Expense
import com.example.kotlinprojectpro.models.Income
import com.example.kotlinprojectpro.ui.main.RecyclerViewAdapter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.auth.User
import kotlinx.android.synthetic.main.fragment_home_horizontal.*
import kotlinx.android.synthetic.main.fragment_home_page.*
import java.util.ArrayList

object FirebaseCommunicator {
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    var mDatabase: DatabaseReference? = null

    init {

        mDatabase = FirebaseDatabase.getInstance().reference

    }

    fun registerWithEmailAndPassword(email: String, password: String, activity: Login) {
        try {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                activity,
                OnCompleteListener { task ->
                    Toaster().registerToast(task.isSuccessful, activity)
                })
        } catch (e: Exception) {
            Toaster().registerToast(false, activity)
        }
    }

    fun loginWithEmailAndPassword(email: String, password: String, activity: Login) {
        try {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
                activity,
                OnCompleteListener { task ->
                    Toaster().loginToast(task.isSuccessful, activity)
                })
        } catch (e: Exception) {
            Toaster().loginToast(false, activity)
        }
    }

    fun getCurrentlyLoggedUserUid(): String? {
        return FirebaseAuth.getInstance().currentUser?.uid
    }

    fun addNewExpenseToDb(newExpense: Expense) {
        Log.i("newExpense", newExpense.toString())
        getCurrentlyLoggedUserUid()?.let {
            mDatabase!!.child("expenses").child(it).push().setValue(
                newExpense
            )
        }
    }

    fun updateGlobalExpensesList() {
        getCurrentlyLoggedUserUid()?.let {
            FirebaseDatabase.getInstance()
                .reference.child("expenses").child(it).addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val expense_list = ArrayList<Any>()
                            globalExpenseList = expense_list
                            for (ds in dataSnapshot.children) {
                                val expense: Expense? =
                                    ds.getValue(Expense::class.java)
                                if (expense != null) {
                                    globalExpenseList.add(expense)
                                }
                            }
                            globalExpenseList.sortByDescending { (it as Expense).date }
                            globalExpenseList
                            val expenses = getAllExpensesValue().toString()
                            globalExpenseList.remove(0)
                            globalExpenseList.add(0, expenses)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }

    fun updateGlobalIncomeList() {
        getCurrentlyLoggedUserUid()?.let {
            FirebaseDatabase.getInstance()
                .reference.child("incomes").child(it).addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            val expense_list = ArrayList<Any>()
                            globalIncomeList = expense_list
                            for (ds in dataSnapshot.children) {
                                val expense: Income? =
                                    ds.getValue(Income::class.java)
                                if (expense != null) {
                                    globalIncomeList.add(expense)
                                }
                            }
                            globalIncomeList.sortByDescending { (it as Income).date }
                            globalIncomeList
                            val expenses = getAllExpensesValue().toString()
                            globalIncomeList.remove(0)
                            globalIncomeList.add(0, expenses)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
        }
    }


    fun addNewIncomeToDb(newIncome: Income){
        getCurrentlyLoggedUserUid()?.let {
            mDatabase!!.child("incomes").child(it).push().setValue(
                newIncome
            )
        }
    }
}