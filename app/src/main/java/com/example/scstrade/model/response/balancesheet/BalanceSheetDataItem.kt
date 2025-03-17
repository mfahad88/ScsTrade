package com.example.scstrade.model.response.balancesheet

data class BalanceSheetDataItem(
    val Cost_Of_Sales: Double,
    val Finance_Cost: Double,
    val Gross_Profit: Double,
    val Operating_Profit: Double,
    val Other_Income: Double,
    val Profit_After_Tax: Double,
    val Profit_Before_Tax: Double,
    val Sales: Double,
    val Taxation: Double,
    val Year: String,
    val company_name: String,
    val quarter_name: String
)