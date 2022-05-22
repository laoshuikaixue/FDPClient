package net.ccbluex.liquidbounce.value

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import java.awt.Color
import kotlin.math.max
import kotlin.math.min
/**
 * Float value represents a value with a float
 */


open class ColorValue(name : String, value: Int) : Value<Int>(name, value) {

    val minimum: Int = -10000000
    val maximum: Int = 1000000
    fun getAwtColor() : Color {
        return Color(value, true);
    }
    fun getHSB() : FloatArray {
        val hsbValues = FloatArray(3)

        val saturation: Float
        val brightness: Float
        var hue: Float

        var cMax: Int = max(value ushr 16 and 0xFF, value ushr 8 and 0xFF)
        if (value and 0xFF > cMax) cMax = value and 0xFF

        var cMin: Int = min(value ushr 16 and 0xFF, value ushr 8 and 0xFF)
        if (value and 0xFF < cMin) cMin = value and 0xFF

        brightness = cMax.toFloat() / 255.0f
        saturation = if (cMax != 0) (cMax - cMin) as Float / cMax.toFloat() else 0F

        if (saturation == 0f) {
            hue = 0f
        } else {
            val redC = (cMax - (value ushr 16 and 0xFF)) as Float / (cMax - cMin) as Float
            // @off
            val greenC = (cMax - (value ushr 8 and 0xFF)) as Float / (cMax - cMin) as Float
            val blueC = (cMax - (value and 0xFF)) as Float / (cMax - cMin) as Float // @on
            hue = (if (value ushr 16 and 0xFF === cMax) blueC - greenC else if (value ushr 8 and 0xFF === cMax) 2.0f + redC - blueC else 4.0f + greenC - redC) / 6.0f
            if (hue < 0) hue += 1.0f
        }

        hsbValues[0] = hue
        hsbValues[1] = saturation
        hsbValues[2] = brightness

        return hsbValues
    }
    fun set(newValue: Number) {
        set(newValue.toFloat())
    }

    override fun toJson() = JsonPrimitive(value)

    override fun fromJson(element: JsonElement) {
        if(element.isJsonPrimitive)
            value = element.asInt

    }
}


