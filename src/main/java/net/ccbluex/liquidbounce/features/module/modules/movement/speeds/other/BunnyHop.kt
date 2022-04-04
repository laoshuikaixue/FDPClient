package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.event.MoveEvent

class BunnyHop : SpeedMode("BunnyHop") {
    fun onJump(event: JumpEvent) {
        if (mc.thePlayer != null) event.cancelEvent()
    }

    override fun onMotion(event: MotionEvent) {
        if (mc.thePlayer.isInWater) return
        if (MovementUtils.isMoving()) {
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump()
                mc.thePlayer.speedInAir = 0.0201f
                mc.timer.timerSpeed = 0.94f
            }
            if (mc.thePlayer.fallDistance > 0.7 && mc.thePlayer.fallDistance < 1.3) {
                mc.thePlayer.speedInAir = 0.02f
                mc.timer.timerSpeed = 1.8f
            }
        }
    }

    override fun onUpdate() {}
    override fun onDisable() {
        mc.thePlayer.speedInAir = 0.02f
        mc.timer.timerSpeed = 1f
    }

    override fun onMove(event: MoveEvent) {
    }
}