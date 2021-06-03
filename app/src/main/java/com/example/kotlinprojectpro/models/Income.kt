package com.example.kotlinprojectpro.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Income(val date: String, val value: String) :
    Parcelable {
    constructor() : this( "", "")
}