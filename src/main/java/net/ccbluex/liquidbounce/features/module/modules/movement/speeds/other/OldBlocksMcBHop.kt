package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.MoveEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.BoolValue

class OldBlocksMcBHop : SpeedMode("OldBlocksMcBHop") {
    private val BoostValue = BoolValue("Boost", true)

    override fun onMove(event: MoveEvent) {
        if (BoostValue.get())
            mc.timer.timerSpeed = 1.2F
        if (!mc.thePlayer.isInWeb && !mc.thePlayer.isInLava && !mc.thePlayer.isInWater && !mc.thePlayer.isOnLadder && mc.thePlayer.ridingEntity == null) {
            if (MovementUtils.isMoving()) {
                mc.gameSettings.keyBindJump.pressed = false
                if (mc.thePlayer.onGround) {
                    MovementUtils.strafe(0.65F)
                    mc.thePlayer.jump()
                    mc.thePlayer.motionY = 0.330999998688698
                    event.y = 0.42
                }
            }
        }
    }
    override fun onDisable() {
        MovementUtils.strafe()
    }
}
