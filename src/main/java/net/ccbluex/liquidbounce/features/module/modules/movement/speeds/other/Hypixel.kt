package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MoveUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.minecraft.potion.Potion

class Hypixel : SpeedMode("Hypixel") {
    override fun onPreMotion() {
        mc.gameSettings.keyBindJump.pressed = false
        if (MovementUtils.isMoving()) {
            if (MoveUtils.isOnGround(0.01) && mc.thePlayer.isCollidedVertically) {
                MoveUtils.setMotion(null, Math.max(0.38, MoveUtils.defaultSpeed() * 0.9))
                if (mc.thePlayer.onGround) {
                    mc.thePlayer.jump()
                    var speed = if (MovementUtils.getSpeed() < 0.38f) MovementUtils.getSpeed() else 0.38f
                    if (mc.thePlayer.onGround && mc.thePlayer.isPotionActive(Potion.moveSpeed)) speed *= 1f + 0.1f * (1 + mc.thePlayer.getActivePotionEffect(
                        Potion.moveSpeed
                    ).amplifier)
                    MovementUtils.strafe(speed)
                    return
                } else if (mc.thePlayer.motionY < 0.2) mc.thePlayer.motionY -= 0.02
                MovementUtils.strafe(0.38f)
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