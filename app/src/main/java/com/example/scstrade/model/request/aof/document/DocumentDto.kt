package com.example.scstrade.model.request.aof.document


import com.google.gson.annotations.SerializedName

data class DocumentDto(
    @SerializedName("accountType")
    val accountType: String,
    @SerializedName("addProof")
    val addProof: String,
    @SerializedName("empAddProof")
    val empAddProof: String,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("identificationType")
    val identificationType: String,
    @SerializedName("signatureProof")
    val signatureProof: String,
    @SerializedName("termsAndCondition")
    val termsAndCondition: String,
    @SerializedName("zakaatDeclaration")
    val zakaatDeclaration: String,
    @SerializedName("zakatStatus")
    val zakatStatus: String
)