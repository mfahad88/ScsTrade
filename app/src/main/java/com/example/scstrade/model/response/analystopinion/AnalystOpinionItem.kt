package com.example.scstrade.model.response.analystopinion


import com.google.gson.annotations.SerializedName

data class AnalystOpinionItem(
    @SerializedName("ao_date")
    val aoDate: String?,
    @SerializedName("ao_heading")
    val aoHeading: String?,
    @SerializedName("ao_text")
    val aoText: String?,
    @SerializedName("ao_type")
    val aoType: String?,
    @SerializedName("company_Name")
    val companyName: String?,
    @SerializedName("link")
    val link: String?,
    @SerializedName("person")
    val person: String?,
    @SerializedName("picturename")
    val picturename: Any?,
    @SerializedName("status")
    val status: Boolean?
)