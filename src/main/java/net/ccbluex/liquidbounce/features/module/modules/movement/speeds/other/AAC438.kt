package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.aac

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils

class AACHop438 : SpeedMode("AACHop4.3.8") {
    override fun onUpdate() {
        val thePlayer = mc.thePlayer ?: return

        mc.timer.timerSpeed = 1.0f

        if (!MovementUtils.isMoving() || thePlayer.isInWater || thePlayer.isInLava ||
            thePlayer.isOnLadder || thePlayer.isRiding) return

        if (thePlayer.onGround)
            thePlayer.jump()
        else {
            if (thePlayer.fallDistance <= 0.1)
                mc.timer.timerSpeed = 1.5f
            else if (thePlayer.fallDistance < 1.3)
                mc.timer.timerSpeed = 0.7f
            else
                mc.timer.timerSpeed = 1f
        }
    }
}