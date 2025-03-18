package com.example.scstrade.model.response.distribution


import com.google.gson.annotations.SerializedName

data class DistributionDataItem(
    @SerializedName("Bonus")
    val bonus: Double,
    @SerializedName("company_name")
    val companyName: String,
    @SerializedName("Dividend")
    val dividend: Double,
    @SerializedName("quarter_name")
    val quarterName: String,
    @SerializedName("Right")
    val right: Double,
    @SerializedName("Year")
    val year: String
)