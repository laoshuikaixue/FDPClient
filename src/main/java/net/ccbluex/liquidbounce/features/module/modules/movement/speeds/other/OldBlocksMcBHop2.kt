/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils

class OldBlocksMcBHop2 : SpeedMode("OldBlocksMcBHop2") {
    override fun onPreMotion() {
        if (!MovementUtils.isMoving() || mc.thePlayer.movementInput.jump) return
        if (mc.thePlayer.onGround) {
            MovementUtils.strafe(0.72f)
            mc.thePlayer.motionY = 0.410999998688698
            return
        }
        MovementUtils.strafe()
    }
}