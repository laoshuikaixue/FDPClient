package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MoveUtils
import net.ccbluex.liquidbounce.utils.MovementUtils.getSpeed
import net.ccbluex.liquidbounce.utils.MovementUtils.isMoving
import net.ccbluex.liquidbounce.utils.MovementUtils.strafe
import net.minecraft.potion.Potion

class OldBHop : SpeedMode("OldBHop") {
    override fun onPreMotion() {
        mc.gameSettings.keyBindJump.pressed = false
        if (isMoving()) {
            if (MoveUtils.isOnGround(0.01) && mc.thePlayer.isCollidedVertically) {
                MoveUtils.setMotion(null, Math.max(0.56, MoveUtils.defaultSpeed() * 0.9))
                mc.thePlayer.jump()
            }
            if (mc.thePlayer.onGround) {
                mc.thePlayer.jump()
                var speed = if (getSpeed() < 0.56f) getSpeed() * 1.000f else 0.56f
                if (mc.thePlayer.onGround && mc.thePlayer.isPotionActive(Potion.moveSpeed)) speed *= 1f + 0.1f * (1 + mc.thePlayer.getActivePotionEffect(
                    Potion.moveSpeed
                ).amplifier)
                strafe(speed)
                return
            } else if (mc.thePlayer.motionY < 0.2) mc.thePlayer.motionY -= 0.02
            strafe(getSpeed() * 1.0f)
        } else {
            mc.thePlayer.motionZ = 0.0
            mc.thePlayer.motionX = mc.thePlayer.motionZ
        }
    }
}