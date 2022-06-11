package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.FloatValue

class BHop : SpeedMode("BHop") {
    private val customStrafeValue = FloatValue("Strafe", 0.52f, 0.2f, 5f)
    private val customTimerValue = FloatValue("Tiemr", 1f, 0.5f, 5f)

    override fun onMotion(event: MotionEvent) {
        if (MovementUtils.isMoving()) {
            mc.timer.timerSpeed = customTimerValue.get()
            if (mc.thePlayer.onGround) {
                MovementUtils.strafe(customStrafeValue.get())
                mc.thePlayer.motionY = 0.2
            } else {
                mc.thePlayer.motionZ = 0.0
                mc.thePlayer.motionX = mc.thePlayer.motionZ
            }
        }
    }
    override fun onDisable() {
        MovementUtils.strafe()
        mc.timer.timerSpeed = 1f
    }
}
