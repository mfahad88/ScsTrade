package com.example.scstrade.model.response.researchreport


import com.google.gson.annotations.SerializedName

data class ResearchReportList(
    @SerializedName("Type")
    val type: String?
)