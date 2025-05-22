package com.example.scstrade.model.response.aof.contactDetails


import com.google.gson.annotations.SerializedName

data class ContactDetailResponse(
    @SerializedName("applicationId")
    val applicationId: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("mailingAddress1")
    val mailingAddress1: String,
    @SerializedName("mailingAddress2")
    val mailingAddress2: String,
    @SerializedName("mailingAddress3")
    val mailingAddress3: String,
    @SerializedName("mailingCityId")
    val mailingCityId: String,
    @SerializedName("mailingCityOther")
    val mailingCityOther: String,
    @SerializedName("mailingCountryId")
    val mailingCountryId: String,
    @SerializedName("mailingProvinceId")
    val mailingProvinceId: String,
    @SerializedName("mailingProvinceOther")
    val mailingProvinceOther: String,
    @SerializedName("mailingResidence")
    val mailingResidence: String,
    @SerializedName("mailingphoneNo")
    val mailingphoneNo: String,
    @SerializedName("permanentAddress1")
    val permanentAddress1: String,
    @SerializedName("permanentAddress2")
    val permanentAddress2: String,
    @SerializedName("permanentAddress3")
    val permanentAddress3: String,
    @SerializedName("permanentCityId")
    val permanentCityId: String,
    @SerializedName("permanentCityOther")
    val permanentCityOther: String,
    @SerializedName("permanentCountryId")
    val permanentCountryId: String,
    @SerializedName("permanentProvinceId")
    val permanentProvinceId: String,
    @SerializedName("permanentProvinceOther")
    val permanentProvinceOther: String,
    @SerializedName("permanentResidence")
    val permanentResidence: String,
    @SerializedName("permanentphoneNo")
    val permanentphoneNo: String
)