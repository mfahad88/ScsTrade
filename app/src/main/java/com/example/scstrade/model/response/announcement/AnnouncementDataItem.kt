package com.example.scstrade.model.response.announcement


import com.google.gson.annotations.SerializedName

data class AnnouncementDataItem(
    @SerializedName("AnnouncementType")
    val announcementType: String?,
    @SerializedName("bm_bc_endd")
    val bmBcEndd: String?,
    @SerializedName("bm_bc_exp")
    val bmBcExp: String?,
    @SerializedName("bm_bc_ld")
    val bmBcLd: String?,
    @SerializedName("bm_bc_startd")
    val bmBcStartd: String?,
    @SerializedName("bm_bonus")
    val bmBonus: String?,
    @SerializedName("bm_date")
    val bmDate: String?,
    @SerializedName("bm_desc")
    val bmDesc: String?,
    @SerializedName("bm_dividend")
    val bmDividend: String?,
    @SerializedName("bm_eps_cum")
    val bmEpsCum: String?,
    @SerializedName("bm_eps_quarter")
    val bmEpsQuarter: String?,
    @SerializedName("bm_ImageLink")
    val bmImageLink: String?,
    @SerializedName("bm_PDFLink")
    val bmPDFLink: String?,
    @SerializedName("bm_place")
    val bmPlace: String?,
    @SerializedName("bm_quarter_number")
    val bmQuarterNumber: String?,
    @SerializedName("bm_right_d")
    val bmRightD: String?,
    @SerializedName("bm_right_p")
    val bmRightP: String?,
    @SerializedName("bm_right_per")
    val bmRightPer: String?,
    @SerializedName("bm_right_price")
    val bmRightPrice: String?,
    @SerializedName("bm_time")
    val bmTime: String?,
    @SerializedName("bm_year")
    val bmYear: String?,
    @SerializedName("company_code")
    val companyCode: String?
)