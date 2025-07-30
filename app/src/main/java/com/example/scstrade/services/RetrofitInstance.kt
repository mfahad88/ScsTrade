package com.example.scstrade.services

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.example.scstrade.helper.NetworkUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.Date
import java.util.concurrent.TimeUnit


object RetrofitInstance {
    val BASE_URL = "https://dataapi.scstrade.com"

    private lateinit var retrofit: Retrofit

    fun init(context: Context) {
        val cacheSize = 10L * 1024 * 1024 // 10 MB
        val cacheDir = File(context.cacheDir, "http_cache")
        val cache = Cache(cacheDir, cacheSize)
        val gson = GsonBuilder()
//            .registerTypeAdapter(String::class.java, StringAdapter())
            .registerTypeAdapter(Int::class.java, IntAdapter())
            .registerTypeAdapter(Float::class.java, FloatAdapter())
            .registerTypeAdapter(Double::class.java, DoubleAdapter())
            .create()
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
            .addInterceptor(CustomLoggingInterceptor())
            .addInterceptor(chuckerInterceptor)
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(provideOfflineCacheInterceptor(context))
            .addNetworkInterceptor(provideOnlineCacheInterceptor())
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    fun <T> create(service: Class<T>): T {
        if (!::retrofit.isInitialized) {
            throw IllegalStateException("ApiClient not initialized. Call ApiClient.init(context) first.")
        }
        return RetrofitInstance.retrofit.create(service)
    }

    // ✅ Serve from cache when offline
    private fun provideOfflineCacheInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!NetworkUtils.isNetworkAvailable(context)) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(1, TimeUnit.DAYS) // Serve stale up to 7 days
                    .build()
                request = request.newBuilder()
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    // ✅ Set cache headers when online
    private fun provideOnlineCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                .maxAge(1, TimeUnit.MINUTES) // Cache response for 5 minutes
                .build()
            response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
    }
    /*val api: ApiService by lazy {
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
