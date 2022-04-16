package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.verus

import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils

class VulcanHop : SpeedMode("VulcanHop") {

    fun onJump(event: JumpEvent) {
        if (mc.thePlayer != null) event.cancelEvent()
    }

    override fun onUpdate() {
        mc.timer.timerSpeed = 1.00f
        if (Math.abs(mc.thePlayer.movementInput.moveStrafe) < 0.1f) {
            mc.thePlayer.jumpMovementFactor = 0.0265f
        }else {
            mc.thePlayer.jumpMovementFactor = 0.0244f
        }
        if (!mc.thePlayer.onGround) {
            mc.gameSettings.keyBindJump.pressed = mc.gameSettings.keyBindJump.isKeyDown
        }
        if (MovementUtils.getSpeed() < 0.215f) {
            MovementUtils.strafe(0.215f)
        }
    }

    override fun onMove(event: MoveEvent) {
        var moveSpeed = Math.max(MovementUtils.getBaseMoveSpeed(), MovementUtils.getSpeed().toDouble())
        if (mc.thePlayer.onGround && MovementUtils.isMoving()) {
            mc.gameSettings.keyBindJump.pressed = false
            mc.thePlayer.jump()
            mc.thePlayer.motionY = 0.42
            event.y = mc.thePlayer.motionY
            moveSpeed *= 1.275
            MovementUtils.strafe()
            if(MovementUtils.getSpeed() < 0.5f) {
                MovementUtils.strafe(0.4849f)
            }
        }else if (!MovementUtils.isMoving()) {
            mc.timer.timerSpeed = 1.00f
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
        }
    }

    override fun onDisable() {
        mc.thePlayer.speedInAir = 0.02f
        mc.timer.timerSpeed = 1.00f
        MovementUtils.strafe()
    }
}