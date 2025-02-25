package com.example.scstrade.model.response.fundamental


import com.google.gson.annotations.SerializedName

data class FundamentalDetailData(
    @SerializedName("company_name")
    val companyName: String,
    @SerializedName("E P/E")
    val ePE: Double,
    @SerializedName("Price")
    val price: Double,
    @SerializedName("Symbol")
    val symbol: String,
    @SerializedName("avg_vol")
    val avgVol: String?
)