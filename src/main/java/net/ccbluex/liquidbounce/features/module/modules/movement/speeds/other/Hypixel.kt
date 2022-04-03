/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/UnlegitMC/FDPClient/
 */
package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MoveUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue

class Hypixel : SpeedMode("Hypixel") {
    override fun onPreMotion() {
        mc.gameSettings.keyBindJump.pressed = false
        if (MovementUtils.isMoving()) {
            if (MoveUtils.isOnGround(0.01) && mc.thePlayer.isCollidedVertically) {
                MovementUtils.strafe(0.165f)
                MoveUtils.setMotion(null, Math.max(0.275, MoveUtils.defaultSpeed() * 0.9))
                mc.thePlayer.jump()
            }
        }else if (!MovementUtils.isMoving()) {
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
        }
    }

    override fun onDisable() {
        MovementUtils.strafe()
    }
}