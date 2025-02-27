package com.example.scstrade.model.response.news


import com.google.gson.annotations.SerializedName

data class NewsData(
    @SerializedName("company_code")
    val companyCode: String,
    @SerializedName("company_name")
    val companyName: String,
    @SerializedName("news_date")
    val newsDate: String,
    @SerializedName("news_desc")
    val newsDesc: String
)