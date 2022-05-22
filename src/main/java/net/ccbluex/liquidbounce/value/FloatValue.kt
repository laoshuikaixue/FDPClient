package net.ccbluex.liquidbounce.value

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

/**
 * Float value represents a value with a float
 */
open class FloatValue(name: String, value: Float, minimum: Float = 0F, maximum: Float = Float.MAX_VALUE) :
    NumberValue<Float>(name, value, minimum, maximum) {

    fun set(newValue: Number) {
        set(newValue.toFloat())
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive) {
            value = element.asFloat
        }
    }
}