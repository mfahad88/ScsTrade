package com.example.scstrade.services

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class IntAdapter : TypeAdapter<Int>() {
    override fun write(out: JsonWriter, value: Int?) {
        out.value(value ?: 0)
    }

    override fun read(reader: JsonReader): Int {
        return if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            0
        } else {
            try {
                reader.nextInt()
            } catch (e: Exception) {
                0
            }
        }
    }
}