package com.example.scstrade.model.response.insider


import com.google.gson.annotations.SerializedName

data class InsiderDataItem(
    @SerializedName("company_code")
    val companyCode: String?,
    @SerializedName("InsiderTransactionAction")
    val insiderTransactionAction: String?,
    @SerializedName("InsiderTransactionDate")
    val insiderTransactionDate: String?,
    @SerializedName("InsiderTransactionDesc")
    val insiderTransactionDesc: String?,
    @SerializedName("InsiderTransactionDesignation")
    val insiderTransactionDesignation: String?,
    @SerializedName("InsiderTransactionForm")
    val insiderTransactionForm: String?,
    @SerializedName("InsiderTransactionImageLink")
    val insiderTransactionImageLink: String?,
    @SerializedName("InsiderTransactionMarket")
    val insiderTransactionMarket: String?,
    @SerializedName("InsiderTransactionName")
    val insiderTransactionName: String?,
    @SerializedName("InsiderTransactionPDFLink")
    val insiderTransactionPDFLink: String?,
    @SerializedName("InsiderTransactionPostDate")
    val insiderTransactionPostDate: String?,
    @SerializedName("InsiderTransactionQuantity")
    val insiderTransactionQuantity: Int?,
    @SerializedName("InsiderTransactionRate")
    val insiderTransactionRate: Double?
)