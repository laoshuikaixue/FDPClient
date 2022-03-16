package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.FloatValue

class MotionPacket : SpeedMode("MotionPacket") {
    private val MotionValue = FloatValue("${valuePrefix}Motion", 0.17f, 0.1f, 2f)

    override fun onMotion(event: MotionEvent) {
        MovementUtils.setMotion(MotionValue.get().toDouble())
    }
}