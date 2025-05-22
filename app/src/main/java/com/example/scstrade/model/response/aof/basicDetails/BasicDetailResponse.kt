package com.example.scstrade.model.response.aof.basicDetails


import com.google.gson.annotations.SerializedName

data class BasicDetailResponse(
    @SerializedName("applicationId")
    val applicationId: String,
    @SerializedName("dateOfBirth")
    val dateOfBirth: String,
    @SerializedName("fatherHusbandName")
    val fatherHusbandName: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("ivrstatus")
    val ivrstatus: String,
    @SerializedName("lifeTime")
    val lifeTime: String,
    @SerializedName("maritalStatus")
    val maritalStatus: String,
    @SerializedName("motherMaidenName")
    val motherMaidenName: String,
    @SerializedName("nationalityId")
    val nationalityId: String,
    @SerializedName("placeOfBirth")
    val placeOfBirth: String,
    @SerializedName("placeOfBirthCity")
    val placeOfBirthCity: String,
    @SerializedName("relationship")
    val relationship: String,
    @SerializedName("salutation")
    val salutation: String,
    @SerializedName("uinExpiryDate")
    val uinExpiryDate: String
)