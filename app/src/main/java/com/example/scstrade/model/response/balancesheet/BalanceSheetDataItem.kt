package com.example.scstrade.model.response.balancesheet


import com.google.gson.annotations.SerializedName

data class BalanceSheetDataItem(
    @SerializedName("Cash")
    val cash: Double?,
    @SerializedName("company_name")
    val companyName: String?,
    @SerializedName("Current Asset")
    val currentAsset: Double?,
    @SerializedName("Current Liability")
    val currentLiability: Double?,
    @SerializedName("Fixed Asset")
    val fixedAsset: Double?,
    @SerializedName("Fixed Liability")
    val fixedLiability: Double?,
    @SerializedName("Inventory")
    val inventory: Double?,
    @SerializedName("Investments")
    val investments: Double?,
    @SerializedName("Paid Up Capital")
    val paidUpCapital: Double?,
    @SerializedName("quarter_name")
    val quarterName: String?,
    @SerializedName("Total Assets")
    val totalAssets: Double?,
    @SerializedName("Total Equity")
    val totalEquity: Double?,
    @SerializedName("Total Liabilities")
    val totalLiabilities: Double?,
    @SerializedName("Year")
    val year: String?
)