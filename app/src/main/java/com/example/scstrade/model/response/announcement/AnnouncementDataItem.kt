package com.example.scstrade.model.response.announcement

data class AnnouncementDataItem(
    val AnnouncementType: String?,
    val Announcement_Date: Any?,
    var name:String?,
    val Bonus: String?,
    val Cumulutive_EPS: String?,
    val Discription: String?,
    val Dividend: String?,
    val Meeting_Date: String?,
    val Meeting_Place: String?,
    val Meeting_Time: String?,
    val Quarter: String?,
    val Quarterly_EPS: String?,
    val Right: String?,
    val Right_Discount: String?,
    val Right_Premium: String?,
    val Right_Price: String?,
    val Year: String?,
    val bm_ImageLink: String?,
    val bm_PDFLink: String?,
    val bm_bc_endd: Any?,
    val bm_bc_ld: Any?,
    val bm_bc_startd: Any?,
    val company_code: String?,
    val x_Price_Date: String?
)