package com.example.scstrade.model.response.snapshot


import com.google.gson.annotations.SerializedName

data class CompanyDetailItem(
    @SerializedName("Address")
    val address: String?,
    @SerializedName("Auditor")
    val auditor: String?,
    @SerializedName("Description")
    val description: String?,
    @SerializedName("Registrar")
    val registrar: String?,
    @SerializedName("Website")
    val website: String?
)