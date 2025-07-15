package com.example.scstrade.model.response.stockscreener


import com.google.gson.annotations.SerializedName

data class StockScreenerItem(
    @SerializedName("Dividend_Yield")
    val dividendYield: Double,
    @SerializedName("Dividend_Yield_Desc")
    val dividendYieldDesc: String,
    @SerializedName("EBITA_Margin")
    val eBITAMargin: Double,
    @SerializedName("EBITA_Margin_Desc")
    val eBITAMarginDesc: String,
    @SerializedName("Enterprise_Value_To_EBITDA")
    val enterpriseValueToEBITDA: Double,
    @SerializedName("Enterprise_Value_To_EBITDA_Desc")
    val enterpriseValueToEBITDADesc: String,
    @SerializedName("Expected_Price_To_Earning")
    val expectedPriceToEarning: Double,
    @SerializedName("Expected_Price_To_Earning_Desc")
    val expectedPriceToEarningDesc: String,
    @SerializedName("Gross_Profit_Margin")
    val grossProfitMargin: Double,
    @SerializedName("Gross_Profit_Margin_Desc")
    val grossProfitMarginDesc: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("Payout_Ratio")
    val payoutRatio: Double,
    @SerializedName("Payout_Ratio_Desc")
    val payoutRatioDesc: String,
    @SerializedName("Price")
    val price: Double,
    @SerializedName("Price_Earning_Growth")
    val priceEarningGrowth: Double,
    @SerializedName("Price_Earning_Growth_Desc")
    val priceEarningGrowthDesc: String,
    @SerializedName("Price_To_Book_Value")
    val priceToBookValue: Double,
    @SerializedName("Price_To_Book_Value_Desc")
    val priceToBookValueDesc: String,
    @SerializedName("Price_To_Earning")
    val priceToEarning: Double,
    @SerializedName("Price_To_Earning_Desc")
    val priceToEarningDesc: String,
    @SerializedName("Return_On_Assets")
    val returnOnAssets: Double,
    @SerializedName("Return_On_Assets_Desc")
    val returnOnAssetsDesc: String,
    @SerializedName("Return_On_Equity")
    val returnOnEquity: Double,
    @SerializedName("Return_On_Equity_Desc")
    val returnOnEquityDesc: String,
    @SerializedName("Symbol")
    val symbol: String,
    @SerializedName("Total_Debt_To_Assets")
    val totalDebtToAssets: Double,
    @SerializedName("Total_Debt_To_Assets_Desc")
    val totalDebtToAssetsDesc: String,
    @SerializedName("Total_Debt_To_Equity")
    val totalDebtToEquity: Double,
    @SerializedName("Total_Debt_To_Equity_Desc")
    val totalDebtToEquityDesc: String
)