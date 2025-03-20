package com.example.scstrade.helper

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

fun downloadPdf(context: Context, url: String, callback: (File?) -> Unit) {
    val client = OkHttpClient()
    val request = Request.Builder().url(url).build()

    Thread {
        try {
            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                callback(null)
                return@Thread
            }

            val file = File(context.cacheDir, "downloaded_pdf.pdf")
            val inputStream = response.body?.byteStream()
            val outputStream = FileOutputStream(file)

            inputStream?.copyTo(outputStream)

            inputStream?.close()
            outputStream.close()

            callback(file)
        } catch (e: Exception) {
            e.printStackTrace()
            callback(null)
        }
    }.start()
}
