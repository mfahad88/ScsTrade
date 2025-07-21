package com.example.scstrade.model.response.news.mettis

data class NewsItem(
    val title: String?,
    val imageUrl: String?,
    val date: String?,
    val time: String?,
    val description: String?,
    val source: String? = "Mettis",
    val newsLink: String?,
    val newsHeading: String? = null
)