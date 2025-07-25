package com.example.scstrade.model.response.researchreport


import com.google.gson.annotations.SerializedName

data class ResearchReportItem(
    @SerializedName("company_code")
    val companyCode: String? = "",
    @SerializedName("company_name")
    val companyName: String? = "",
    @SerializedName("file_date")
    val fileDate: String? = "",
    @SerializedName("FileTitle")
    val fileTitle: String? = "",
    @SerializedName("FileVPath")
    val fileVPath: String? = "",
    @SerializedName("ReportType")
    val reportType: String? = ""
)