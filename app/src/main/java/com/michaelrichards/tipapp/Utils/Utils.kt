package com.michaelrichards.tipapp.Utils



fun calculateTip(billAmount: Double, tipPercentage : Float): Double {
    return if (billAmount > 0 && billAmount.toString().isNotEmpty())(billAmount* (tipPercentage/100)) else 0.0
}

fun calcTotalPerPerson(billAmount: Double, splitBy: Int, tipPercentage: Float): Double{
    val bill = calculateTip(billAmount, tipPercentage) + billAmount
    return bill/splitBy
}