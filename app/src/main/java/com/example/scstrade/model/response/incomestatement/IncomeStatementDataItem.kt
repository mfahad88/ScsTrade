package com.example.scstrade.model.response.incomestatement


import com.google.gson.annotations.SerializedName

data class IncomeStatementDataItem(
    @SerializedName("company_name")
    val companyName: String,
    @SerializedName("Cost Of Sales")
    val costOfSales: Double,
    @SerializedName("Finance Cost")
    val financeCost: Double,
    @SerializedName("Gross Profit")
    val grossProfit: Double,
    @SerializedName("Operating Profit")
    val operatingProfit: Double,
    @SerializedName("Other Income")
    val otherIncome: Double,
    @SerializedName("Profit After Tax")
    val profitAfterTax: Double,
    @SerializedName("Profit Before Tax")
    val profitBeforeTax: Double,
    @SerializedName("quarter_name")
    val quarterName: String,
    @SerializedName("Sales")
    val sales: Double,
    @SerializedName("Taxation")
    val taxation: Double,
    @SerializedName("Year")
    val year: String
)