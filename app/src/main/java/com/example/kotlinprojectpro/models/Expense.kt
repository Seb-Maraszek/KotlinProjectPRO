package com.example.kotlinprojectpro.models

import android.os.Parcelable
import android.util.Log
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Expense(val category: String, val title: String, val image: String, val date: String, val value: String) :
    Parcelable, DatabaseReference.CompletionListener {
    constructor() : this("","", "", "", "")

    override fun onComplete(error: DatabaseError?, ref: DatabaseReference) {
      Log.i("Cannot delete", error.toString())
    }
}



