package com.example.scstrade.model.response.snapshot


import com.google.gson.annotations.SerializedName

data class Overview(
    @SerializedName("Authorized_Capital")
    val authorizedCapital: String,
    @SerializedName("Avg_Volume_12M")
    val avgVolume12M: String,
    @SerializedName("Beta")
    val beta: String,
    @SerializedName("Description")
    val description: String,
    @SerializedName("Face_Value")
    val faceValue: String,
    @SerializedName("Free_Float")
    val freeFloat: String,
    @SerializedName("Free_Float_Per")
    val freeFloatPer: String,
    @SerializedName("Market_Cap")
    val marketCap: String,
    @SerializedName("One_Month_High")
    val oneMonthHigh: String,
    @SerializedName("One_Month_Low")
    val oneMonthLow: String,
    @SerializedName("One_Month_Return")
    val oneMonthReturn: String,
    @SerializedName("Paid_Up_Capital")
    val paidUpCapital: String,
    @SerializedName("Six_Month_High")
    val sixMonthHigh: String,
    @SerializedName("Six_Month_Low")
    val sixMonthLow: String,
    @SerializedName("Six_Month_Return")
    val sixMonthReturn: String,
    @SerializedName("Symbol")
    val symbol: Any?,
    @SerializedName("Total_No_Shares")
    val totalNoShares: String,
    @SerializedName("Twelve_Month_High")
    val twelveMonthHigh: String,
    @SerializedName("Twelve_Month_Low")
    val twelveMonthLow: String,
    @SerializedName("Twelve_Month_Return")
    val twelveMonthReturn: String,
    @SerializedName("Two_Month_Return")
    val twoMonthReturn: String,
    @SerializedName("Year_End")
    val yearEnd: String
)