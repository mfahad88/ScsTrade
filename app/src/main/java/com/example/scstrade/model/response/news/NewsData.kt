package com.example.scstrade.model.response.news


import com.google.gson.annotations.SerializedName

data class NewsData(
    @SerializedName("news_date")
    val newsDate: String?,
    @SerializedName("news_heading")
    val newsHeading: String,
    @SerializedName("news_link")
    val newsLink: String,
    @SerializedName("news_text")
    val newsText: String,
    @SerializedName("Type")
    val type: String
)