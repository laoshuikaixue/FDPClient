package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MoveUtils
import net.ccbluex.liquidbounce.utils.MovementUtils

class VulCan : SpeedMode("VulCan") {
    fun onJump(event: JumpEvent) {
        if (mc.thePlayer != null) event.cancelEvent()
    }

    override fun onUpdate() {
        if (!MovementUtils.isMoving()) {
            return
        }
        if (mc.thePlayer.onGround) {
            mc.gameSettings.keyBindJump.pressed = false
            MoveUtils.setMotion(null, Math.max(0.275, MoveUtils.defaultSpeed() * 0.5))
            mc.thePlayer.jump()
            mc.timer.timerSpeed = 1.00F
        }
        if (mc.thePlayer.fallDistance > 0.7 && mc.thePlayer.fallDistance < 1.3) {
            mc.timer.timerSpeed = 1.08F
        }
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1f
    }
}
