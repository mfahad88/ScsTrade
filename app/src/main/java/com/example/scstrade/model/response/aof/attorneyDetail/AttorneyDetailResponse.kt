package com.example.scstrade.model.response.aof.attorneyDetail

import com.google.gson.annotations.SerializedName

data class AttorneyDetailResponse (
    @SerializedName("attorneyType")
    val attorneyType: String?,
    @SerializedName("clientNameAtr")
    val clientNameAtr: String?,
    @SerializedName("cnicAtr")
    val cnicAtr: String?,
    @SerializedName("cnicExpiryDateAtr")
    val cnicExpiryDateAtr: String?,
    @SerializedName("cnicLifeTimeAtr")
    val cnicLifeTimeAtr: String?,
    @SerializedName("emailAtr")
    val emailAtr: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("identificationAtr")
    val identificationAtr: String?,
    @SerializedName("landlineAtr")
    val landlineAtr: String?,
    @SerializedName("mailingAddressAtr1")
    val mailingAddressAtr1: String?,
    @SerializedName("mailingAddressAtr2")
    val mailingAddressAtr2: String?,
    @SerializedName("mailingAddressAtr3")
    val mailingAddressAtr3: String?,
    @SerializedName("mailingCityAtr")
    val mailingCityAtr: String?,
    @SerializedName("mailingCountryAtr")
    val mailingCountryAtr: String?,
    @SerializedName("mailingProvinceAtr")
    val mailingProvinceAtr: String?,
    @SerializedName("mobileAtr")
    val mobileAtr: String?,
    @SerializedName("otherMailingCityAtr")
    val otherMailingCityAtr: String?,
    @SerializedName("OtherMailingProvAtr")
    val otherMailingProvAtr: String?,
    @SerializedName("salutationAtr")
    val salutationAtr: String?
)