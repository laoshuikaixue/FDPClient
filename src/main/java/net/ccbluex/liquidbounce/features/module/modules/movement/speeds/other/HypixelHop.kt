package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
//import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MovementUtils

class HypixelHop : SpeedMode("HypixelHop") {
    fun onJump(event: JumpEvent) {
        if (mc.thePlayer != null) event.cancelEvent()
    }

    override fun onMove(event: MoveEvent) {
        if (!mc.thePlayer.isInWeb && !mc.thePlayer.isInLava && !mc.thePlayer.isInWater && !mc.thePlayer.isOnLadder && mc.thePlayer.ridingEntity == null) {
            if (MovementUtils.isMoving()) {
                mc.gameSettings.keyBindJump.pressed = false
                if (mc.thePlayer.onGround) {
                    MovementUtils.strafe(0.364f)
                    mc.thePlayer.speedInAir = 0.0223f
                    mc.thePlayer.jump()
                    mc.thePlayer.motionY = 0.032
//                    ClientUtils.displayChatMessage("MotionY:"+mc.thePlayer.motionY.toString())
                    event.y = 0.42
                }
            }
        }
    }
    override fun onDisable() {
        mc.thePlayer.speedInAir = 0.02f
        MovementUtils.strafe()
    }
}
