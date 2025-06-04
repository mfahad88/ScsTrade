package com.example.scstrade.services

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.scstrade.services.RetrofitInstance.BASE_URL
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit

object RetrofitInstanceAof {
    private lateinit var retrofit: Retrofit

    fun init(context: Context) {
        val chuckerInterceptor = ChuckerInterceptor.Builder(context)
            .collector(ChuckerCollector(context))
            .maxContentLength(250_000L)
            .redactHeaders("Authorization")
            .alwaysReadResponseBody(true)
            .build()

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
//            .addInterceptor(chuckerInterceptor)
            .addInterceptor(AuthInterceptor(context))
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl("https://aof.scstrade.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    fun <T> create(service: Class<T>): T {
        if (!::retrofit.isInitialized) {
            throw IllegalStateException("ApiClient not initialized. Call ApiClient.init(context) first.")
        }
        return retrofit.create(service)
    }
    /*val BASE_URL = "http://109.236.90.87:3000/"

    val api: ApiService by lazy {
        val interceptor= HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
        val gson: Gson = GsonBuilder()
            .registerTypeAdapter(Date::class.java, DateDeserializer())
//            .registerTypeAdapter(String::class.java,StringTypeAdapter())
            .create()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson)) // JSON to Kotlin object
            .build()

            .create(ApiService::class.java)
    }*/
}