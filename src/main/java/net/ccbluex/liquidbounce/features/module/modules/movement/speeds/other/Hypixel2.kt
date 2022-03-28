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

class Hypixel2 : SpeedMode("Hypixel2") {
    private var stage = 0

    val fastfall = BoolValue("FastFall", false)

    override fun onPreMotion() {
        mc.gameSettings.keyBindJump.pressed = false
        if (MovementUtils.isMoving()) {
            if (MoveUtils.isOnGround(0.01)) {
                mc.thePlayer.jump()
                mc.timer.timerSpeed=1.1f
                stage = 0
            }
        } else {
            if (fastfall.get()) {
                stage++
                if (stage == 3) {
                    mc.thePlayer.motionY -= 0.05
                }
                if (stage == 5) {
                    mc.thePlayer.motionY -= 0.184
                }
            }
            mc.timer.timerSpeed -= 0.004f

            MovementUtils.setSpeed3(MovementUtils.getBaseMoveSpeed().toInt() * MovementUtils.getSpeed().toInt())
        }
    }
}