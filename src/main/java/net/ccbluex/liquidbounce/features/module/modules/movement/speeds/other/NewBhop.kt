package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils

class NewBhop : SpeedMode("HypixelNewHop") {

    fun onJump(event: JumpEvent) {
        if (mc.thePlayer != null) event.cancelEvent()
    }

    override fun onDisable() {
        mc.thePlayer.speedInAir = 0.02f
        mc.timer.timerSpeed = 1f
    }

    override fun onMove(event: MoveEvent) {
        if (!mc.thePlayer.isInWeb && !mc.thePlayer.isInLava && !mc.thePlayer.isInWater && !mc.thePlayer.isOnLadder && mc.thePlayer.ridingEntity == null) {
            if (MovementUtils.isMoving()) {
                var moveSpeed = Math.max(MovementUtils.getBaseMoveSpeed(), MovementUtils.getSpeed().toDouble())
                if (mc.thePlayer.onGround && !mc.gameSettings.keyBindJump.isKeyDown) {
                    mc.thePlayer.jump()
                    mc.thePlayer.motionY = 0.42
                    event.y = mc.thePlayer.motionY
                    moveSpeed *= 1.475
                }
            }
        }
    }
}
