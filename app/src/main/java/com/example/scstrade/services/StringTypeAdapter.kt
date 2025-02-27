package com.example.scstrade.services

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class StringTypeAdapter(): TypeAdapter<String>() {
    override fun write(out: JsonWriter?, value: String?) {
        out?.value(value)?:out?.nullValue()
    }

    override fun read(reader: JsonReader): String {
        if(reader.peek() == JsonToken.STRING){

            val value:String=reader.nextString()
            if(value.isBlank()){
                return "0.0"
            }else{

                return value
            }
        } else {
            return reader.nextString()
        }
    }

    fun containsNumberOrDecimal(input: String): Boolean {
        return input.matches(".*\\d.*".toRegex()) // Checks if string contains any digit (0-9) or decimal
    }
}