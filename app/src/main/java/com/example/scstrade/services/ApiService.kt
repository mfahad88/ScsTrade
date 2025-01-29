package com.example.scstrade.services

import com.example.scstrade.model.summary.KSEIndices
import retrofit2.http.GET

interface ApiService {

    @GET(value = "/Data?que=KSE Indices")
    suspend fun getIndices():List<KSEIndices>
}