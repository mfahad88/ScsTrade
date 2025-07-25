package com.example.scstrade.model.response.notification


import com.google.gson.annotations.SerializedName

data class NotificationDto(
    @SerializedName("AnnouncementTypeName")
    val announcementTypeName: String?,
    @SerializedName("company_code")
    val companyCode: String?,
    @SerializedName("company_name")
    val companyName: String?,
    @SerializedName("MainAnnCompany")
    val mainAnnCompany: String?,
    @SerializedName("MainAnnDate")
    val mainAnnDate: String?,
    @SerializedName("MainAnnDesc")
    val mainAnnDesc: String?,
    @SerializedName("MainAnnDetails")
    val mainAnnDetails: String?,
    @SerializedName("MainAnnHeading")
    val mainAnnHeading: String?,
    @SerializedName("MainAnnID")
    val mainAnnID: Int?,
    @SerializedName("MainAnnIDRef")
    val mainAnnIDRef: Int?,
    @SerializedName("MainAnnLinkIMG")
    val mainAnnLinkIMG: String?,
    @SerializedName("MainAnnLinkPDF")
    val mainAnnLinkPDF: String?,
    @SerializedName("MainAnnSource")
    val mainAnnSource: String?,
    @SerializedName("MainAnnStatus")
    val mainAnnStatus: Boolean?,
    @SerializedName("MainAnnType")
    val mainAnnType: Int?
)