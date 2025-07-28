package com.example.scstrade.services
import com.google.gson.*
import com.google.gson.stream.*
class DoubleAdapter : TypeAdapter<Double>() {
    override fun write(out: JsonWriter, value: Double?) {
        out.value(value ?: 0.0)
    }

    override fun read(reader: JsonReader): Double {
        return if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            0.0
        } else {
            try {
                reader.nextDouble()
            } catch (e: Exception) {
                0.0
            }
        }
    }
}