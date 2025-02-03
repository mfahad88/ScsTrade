package com.example.scstrade.model.response.stock


import com.google.gson.annotations.SerializedName

data class StockItem(
    @SerializedName("AP")
    val aP: Double,
    @SerializedName("AV")
    val aV: Int,
    @SerializedName("AvgP")
    val avgP: Double,
    @SerializedName("BP")
    val bP: Double,
    @SerializedName("BV")
    val bV: Int,
    @SerializedName("CH")
    val cH: Double,
    @SerializedName("CHP")
    val cHP: Double,
    @SerializedName("CL")
    val cL: Double,
    @SerializedName("company_logo")
    val companyLogo: String,
    @SerializedName("HP")
    val hP: Double,
    @SerializedName("high52")
    val high52: String,
    @SerializedName("IN")
    val iN: String,
    @SerializedName("LP")
    val lP: Double,
    @SerializedName("low52")
    val low52: String,
    @SerializedName("NM")
    val nM: String,
    @SerializedName("OC")
    val oC: Double,
    @SerializedName("SN")
    val sN: String,
    @SerializedName("SYM")
    val sYM: String,
    @SerializedName("V")
    val v: Int
)