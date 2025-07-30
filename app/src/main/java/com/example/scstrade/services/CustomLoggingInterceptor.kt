package com.example.scstrade.services

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class CustomLoggingInterceptor : Interceptor {
    private val excludedEndpoints = listOf("/Data?que=KSE Indices", "/Data?que=AllData", "/Data?que=FutureData")

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        // Check if request URL contains any excluded endpoints
        if (excludedEndpoints.any { url.contains(it) }) {
            return chain.proceed(request) // Skip logging
        }

        // Log the request manually
        Log.d("Retrofit", "➡️ Request: ${request.method} ${request.url}")
        request.headers.forEach {
            Log.d("Retrofit", "Header: ${it.first} = ${it.second}")
        }

        request.body?.let { body ->
            val buffer = okio.Buffer()
            body.writeTo(buffer)
            val bodyString = buffer.readUtf8()
            Log.d("Retrofit", "Request Body: $bodyString")
        }

        // Proceed and log the response
        val response = chain.proceed(request)
        val responseBody = response.peekBody(Long.MAX_VALUE)
        Log.d("Retrofit", "⬅️ Response (${response.code}): ${response.request.url}")
        Log.d("Retrofit", "Response Body: ${responseBody.string()}")

        return response
    }
}
