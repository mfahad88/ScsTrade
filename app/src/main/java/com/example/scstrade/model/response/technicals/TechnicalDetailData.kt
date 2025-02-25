package com.example.scstrade.model.response.technicals


import com.google.gson.annotations.SerializedName

data class TechnicalDetailData(
    @SerializedName("company_name")
    val companyName: String,
    @SerializedName("Initiated")
    val initiated: String,
    @SerializedName("Initiated At")
    val initiatedAt: String,
    @SerializedName("Signal")
    val signal: String,
    @SerializedName("Symbol")
    val symbol: String
)