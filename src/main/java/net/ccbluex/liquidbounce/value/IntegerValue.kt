package net.ccbluex.liquidbounce.value

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive

/**
 * Integer value represents a value with a integer
 */
open class IntegerValue(name: String, value: Int, minimum: Int = 0, maximum: Int = Integer.MAX_VALUE) :
    NumberValue<Int>(name, value, minimum, maximum) {

    fun set(newValue: Number) {
        set(newValue.toInt())
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if (element.isJsonPrimitive) {
            value = element.asInt
        }
    }
}