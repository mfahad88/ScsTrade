package com.example.scstrade.model.response.portfolio


import com.google.gson.annotations.SerializedName

data class PortfolioDetailItem(
    @SerializedName("PortfolioCommission")
    val portfolioCommission: Double,
    @SerializedName("PortfolioCommissionType")
    val portfolioCommissionType: String,
    @SerializedName("PortfolioDate")
    val portfolioDate: String,
    @SerializedName("PortfolioDetailID")
    val portfolioDetailID: Int,
    @SerializedName("PortfolioMainID")
    val portfolioMainID: Int,
    @SerializedName("PortfolioPosition")
    val portfolioPosition: Int,
    @SerializedName("PortfolioQuantity")
    val portfolioQuantity: Int,
    @SerializedName("PortfolioRate")
    val portfolioRate: Double,
    @SerializedName("PortfolioSymbol")
    val portfolioSymbol: String,
    @SerializedName("PortfolioType")
    val portfolioType: String
)