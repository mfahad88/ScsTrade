package com.example.scstrade.model.request.aof.otherDetail


import com.google.gson.annotations.SerializedName

data class OtherDetailDto(
    @SerializedName("accountType")
    val accountType: String,
    @SerializedName("annualIncomeNormal")
    val annualIncomeNormal: String,
    @SerializedName("department")
    val department: String,
    @SerializedName("employeeAddress")
    val employeeAddress: String,
    @SerializedName("employeeName")
    val employeeName: String,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("jobTitle")
    val jobTitle: String,
    @SerializedName("occupation")
    val occupation: String,
    @SerializedName("otherOccupation")
    val otherOccupation: String,
    @SerializedName("remittanceBasis")
    val remittanceBasis: String,
    @SerializedName("sourceOfIncome")
    val sourceOfIncome: String,
    @SerializedName("zakatStatus")
    val zakatStatus: String
)