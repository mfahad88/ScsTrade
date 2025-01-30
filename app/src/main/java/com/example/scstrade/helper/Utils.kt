package com.example.scstrade.helper

import com.google.gson.JsonParseException
import java.util.Date

class Utils {
    companion object{
        fun helloWorld(){
            print("Hello World!")
        }

        fun convertToMillions(value: Double): String {
            return if (value >= 1_000_000) {
                // Convert to millions and append "M"
                val millions = value / 1_000_000.0
                String.format("%.1fM", millions)
            } else {
                // Return the original value if it's less than a million
                value.toString()
            }
        }

        fun convertDate(dateString:String): Date {
            if (dateString != null && dateString.startsWith("/Date(") && dateString.endsWith(")/")) {
                val timestamp = dateString.substring(6, dateString.length - 2)
                val millis = timestamp.toLong()
                return Date(millis)
            }else{
                return  Date()
            }
         }
    }

}