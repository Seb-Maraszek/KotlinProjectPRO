package com.example.kotlinprojectpro

data class UserData(
    var currency:String? = "PLN",
    var currentMoney: Float? = 0.0f,
    var billingPeriodDays: Int? = 10,
    var totalPredictedIncome:Float? = 100f,
    var totalPredictedOutcome:Float? = 100f,
    )
