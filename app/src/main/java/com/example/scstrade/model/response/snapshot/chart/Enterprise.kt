package com.example.scstrade.model.response.snapshot.chart


import com.google.gson.annotations.SerializedName

data class Enterprise(
    @SerializedName("EP_Value")
    val ePValue: List<Double>,
    @SerializedName("EV__EBITDA")
    val eVEBITDA: List<Double>,
    @SerializedName("Market_Cap")
    val marketCap: List<Double>,
    @SerializedName("Year")
    val year: List<String>
)