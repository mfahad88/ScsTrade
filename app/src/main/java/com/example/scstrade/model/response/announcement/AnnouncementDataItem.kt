package com.example.scstrade.model.response.announcement


import com.google.gson.annotations.SerializedName

data class AnnouncementDataItem(
    var name:String,
    @SerializedName("Action")
    val action: String,
    @SerializedName("Announcement_Date")
    val announcementDate: String,
    @SerializedName("Announcement_Type")
    val announcementType: String,
    @SerializedName("Bonus")
    val bonus: String,
    @SerializedName("company_code")
    val companyCode: String,
    @SerializedName("Cumulutive_EPS")
    val cumulutiveEPS: String,
    @SerializedName("Designation")
    val designation: String,
    @SerializedName("Discription")
    val discription: String,
    @SerializedName("Dividend")
    val dividend: String,
    @SerializedName("Form_of_Shares")
    val formOfShares: String,
    @SerializedName("ImageLink")
    val imageLink: String,
    @SerializedName("Insider_Name")
    val insiderName: String,
    @SerializedName("Market")
    val market: String,
    @SerializedName("Meeting_Date")
    val meetingDate: String,
    @SerializedName("Meeting_Place")
    val meetingPlace: String,
    @SerializedName("Meeting_Time")
    val meetingTime: String,
    @SerializedName("PDFLink")
    val pDFLink: String,
    @SerializedName("Post_Date")
    val postDate: String,
    @SerializedName("Quantity")
    val quantity: String,
    @SerializedName("Quarter")
    val quarter: String,
    @SerializedName("Quarterly_EPS")
    val quarterlyEPS: String,
    @SerializedName("Rate")
    val rate: String,
    @SerializedName("Right")
    val right: String,
    @SerializedName("Right_Discount")
    val rightDiscount: String,
    @SerializedName("Right_Premium")
    val rightPremium: String,
    @SerializedName("Right_Price")
    val rightPrice: String,
    @SerializedName("Transaction_Date")
    val transactionDate: String,
    @SerializedName("Type")
    val type: String,
    @SerializedName("x_Price_Date")
    val xPriceDate: String,
    @SerializedName("Year")
    val year: String
)