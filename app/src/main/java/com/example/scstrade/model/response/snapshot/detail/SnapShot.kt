package com.example.scstrade.model.response.snapshot.detail


import com.google.gson.annotations.SerializedName

data class SnapShot(
    @SerializedName("Advances_And_Deposits")
    val advancesAndDeposits: List<DescNameValue>?,
    @SerializedName("Cash")
    val cash: List<DescNameValue>?,
    @SerializedName("Dividend")
    val dividend: List<DescNameValue>?,
    @SerializedName("Earnings")
    val earnings: List<DescNameValue>?,
    @SerializedName("Enterprise_Value")
    val enterpriseValue: List<DescNameValue>?,
    @SerializedName("Equity")
    val equity: List<DescNameValue>?,
    @SerializedName("Important_Ratios")
    val importantRatios: List<DescNameValue>?,
    @SerializedName("Insurance")
    val insurance: List<DescNameValue>?,
    @SerializedName("Liquidity")
    val liquidity: List<DescNameValue>?,
    @SerializedName("Net_Asset_Value_NAV")
    val netAssetValueNAV: List<DescNameValue>?,
    @SerializedName("Profitablility")
    val profitablility: List<DescNameValue>?,
    @SerializedName("Sales")
    val sales: List<DescNameValue>?,
    @SerializedName("Solvency")
    val solvency: List<DescNameValue>?
)