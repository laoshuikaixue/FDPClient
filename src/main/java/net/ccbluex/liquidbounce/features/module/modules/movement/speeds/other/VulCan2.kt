package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.JumpEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MoveUtils
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.utils.PlayerUtils
import net.minecraft.block.BlockAir

class VulCan2 : SpeedMode("VulCan2") {
    var bool = false
    var offGroundTicks = 0

    override fun onEnable() {
        bool = false
        offGroundTicks = 0
    }

    fun onJump(event: JumpEvent) {
        if (mc.thePlayer != null) event.cancelEvent()
    }

    override fun onUpdate() {
        if (mc.thePlayer.onGround) {
            offGroundTicks = 0
        } else {
            offGroundTicks += 1
        }
        if (!MovementUtils.isMoving()) {
            if (mc.thePlayer.onGround) {
                val speed: Double = MovementUtils.getBaseMoveSpeed() - 0.01
                MoveUtils.strafe(speed - Math.random() / 2000)
                mc.thePlayer.jump()
                bool = true
            }else{
                if (bool) {
                    if (offGroundTicks > 3)
                        mc.thePlayer.motionY = MovementUtils.getPredictedMotionY(mc.thePlayer.motionY);

                    if (PlayerUtils.getBlockRelativeToPlayer(0.0, 2.0, 0.0) is BlockAir)
                        MovementUtils.strafe(MovementUtils.getSpeed() * (1.1 - (Math.random() / 500)).toFloat())
                }

                if (mc.thePlayer.hurtTime == 9)
                    MovementUtils.strafe();
            }
        }
    }
}