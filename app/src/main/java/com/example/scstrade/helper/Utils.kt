package com.example.scstrade.helper

import java.util.Date

class Utils {
    companion object{
        fun helloWorld(){
            print("Hello World!")
        }

        fun convertToMillions(value: Double): String {
            return /*if(value>=1_000_000_000.0){
                String.format("%.1f Bn", value / 1_000_000_000.0)
            }else*/ if (value >= 1_000_000) {
                // Convert to millions and append "M"
                String.format("%.1fm", value / 1_000_000.0)
            } else {
                // Return the original value if it's less than a million
                value.toString()
            }

        }

        fun convertDate(dateString:String): Long {

            if (dateString != null && dateString.startsWith("/Date(") && dateString.endsWith(")/")) {
                val timestamp = dateString.substring(6, dateString.length - 2).toLong()
                return timestamp / (5 * 60 * 1000) * (5 * 60 * 1000)

            }else{
                return  0
            }
         }
    }

}