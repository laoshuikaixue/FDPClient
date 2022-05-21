/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue

class OldBlocksMcBHop3 : SpeedMode("OldBlocksMcBHop3") {
    private val BoostValue = BoolValue("Boost", true)

    override fun onMotion(event: MotionEvent) {
        if (MovementUtils.isMoving()) {
            mc.timer.timerSpeed = 1F
            if (mc.thePlayer.onGround) {
                MovementUtils.strafe(0.65F)
                mc.thePlayer.motionY = 0.2
            } else if (BoostValue.get()) MovementUtils.strafe(0.65F) else MovementUtils.strafe()
        } else {
            mc.thePlayer.motionZ = 0.0
            mc.thePlayer.motionX = mc.thePlayer.motionZ
        }

    }
    override fun onDisable() {
        MovementUtils.strafe()
        mc.timer.timerSpeed = 1f
    }
}
