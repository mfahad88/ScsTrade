package com.example.scstrade.model.response.technicals


import com.google.gson.annotations.SerializedName

data class TechnicalData(
    @SerializedName("Technicals")
    val technicals: String,
    @SerializedName("ViewType")
    val viewType: String
)