package com.example.scstrade.services
import com.google.gson.*
import com.google.gson.stream.*
class FloatAdapter : TypeAdapter<Float>() {
    override fun write(out: JsonWriter, value: Float?) {
        out.value(value ?: 0.0f)
    }

    override fun read(reader: JsonReader): Float {
        return if (reader.peek() == JsonToken.NULL) {
            reader.nextNull()
            0.0f
        } else {
            try {
                reader.nextDouble().toFloat()
            } catch (e: Exception) {
                0.0f
            }
        }
    }
}