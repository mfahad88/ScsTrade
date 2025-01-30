package com.example.scstrade.services

import android.util.Log
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : JsonDeserializer<Date> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: com.google.gson.JsonDeserializationContext?): Date? {
        val dateString = json?.asString
        if (dateString != null && dateString.startsWith("/Date(") && dateString.endsWith(")/")) {
            try {
                // Extract timestamp from the /Date(...) format
                val timestamp = dateString.substring(6, dateString.length - 2)
                val millis = timestamp.toLong()
                val date=Date(millis)
                System.out.println(date)
                return date
            } catch (e: NumberFormatException) {
                throw JsonParseException("Invalid date format", e)
            }
        } else {
            throw JsonParseException("Invalid date format")
        }
    }
}
