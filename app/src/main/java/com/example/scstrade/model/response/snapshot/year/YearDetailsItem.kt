package com.example.scstrade.model.response.snapshot.year


import com.google.gson.annotations.SerializedName

data class YearDetailsItem(
    @SerializedName("company_code")
    val companyCode: String,
    @SerializedName("quarter_name")
    val quarterName: String,
    @SerializedName("quarter_number")
    val quarterNumber: String,
    @SerializedName("yeartext")
    val yeartext: String
)