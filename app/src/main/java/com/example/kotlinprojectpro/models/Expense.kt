package com.example.kotlinprojectpro.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Expense(val category: String, val title: String, val image: String, val date: String, val value: String) :
    Parcelable {
    constructor() : this("","", "", "", "")
}



