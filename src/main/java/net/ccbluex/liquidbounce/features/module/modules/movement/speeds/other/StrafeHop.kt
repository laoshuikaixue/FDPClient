/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils

class StrafeHop : SpeedMode("StrafeHop") {
    override fun onPreMotion() {
        if (MovementUtils.isMoving()) {
            if (mc.thePlayer.onGround) mc.thePlayer.jump() else MovementUtils.strafe()
        } else {
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
        }
    }
}
