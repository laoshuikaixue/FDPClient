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
    private val onGround = BoolValue("OnGround", true)
    override fun onPreMotion() {
        if (MovementUtils.isMoving()) {
            if (MoveUtils.isOnGround(0.01) && mc.thePlayer.isCollidedVertically) {
                MoveUtils.setMotion(null, Math.max(0.275, MoveUtils.defaultSpeed() * 0.9))
                mc.thePlayer.jump()
            } else if (!onGround.get()) {
                MoveUtils.setMotion(null, MovementUtils.getSpeed2())
            }
        }
    }
}