package com.example.scstrade.services

import android.content.Context
import com.example.scstrade.helper.AppConstants
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val prefs = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val token = prefs.getString(AppConstants.ACCESS_TOKEN, null)

        val requestBuilder = chain.request().newBuilder()

        token?.let {
            requestBuilder.addHeader("Authorization", "Bearer $it")
        }
        return chain.proceed(requestBuilder.build())
    }
}