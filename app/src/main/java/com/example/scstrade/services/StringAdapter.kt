package com.example.scstrade.services

import com.google.gson.*
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class StringAdapter : TypeAdapter<String>() {
    override fun write(out: JsonWriter, value: String?) {
        val clean = if (value.isNullOrBlank()) "N/A" else value
        out.value(clean)
    }

    override fun read(reader: JsonReader): String {
        return when (reader.peek()) {
            JsonToken.NULL -> {
                reader.nextNull()
                "N/A"
            }
            JsonToken.STRING -> {
                val str = reader.nextString()
                if (str.isBlank()) "N/A" else str
            }
            else -> {
                reader.skipValue()
                "N/A"
            }
        }
    }
}