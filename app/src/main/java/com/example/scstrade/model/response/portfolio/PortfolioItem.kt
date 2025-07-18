package com.example.scstrade.model.response.portfolio


import com.google.gson.annotations.SerializedName


data class PortfolioItem(
    @SerializedName("PortfolioMainDefault")
    val portfolioMainDefault: Boolean,
    @SerializedName("PortfolioMainID")
    val portfolioMainID: Int,
    @SerializedName("PortfolioMainName")
    val portfolioMainName: String,
    @SerializedName("PortfolioMainPosition")
    val portfolioMainPosition: Int,
    @SerializedName("RegistrationID")
    val registrationID: Int,

    var numberCompany: Int
)