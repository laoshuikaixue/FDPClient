package net.ccbluex.liquidbounce.features.module.modules.movement.speeds.other

import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.speeds.SpeedMode
import net.ccbluex.liquidbounce.utils.MovementUtils.getBaseMoveSpeed
import net.ccbluex.liquidbounce.utils.MovementUtils.strafe
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.client.C03PacketPlayer.C06PacketPlayerPosLook

class Vulcan3 : SpeedMode("Vulcan3") {
    private var offGroundTicks = 0

    override fun onEnable() {
        offGroundTicks = 0
    }

    override fun onMotion(event: MotionEvent) {
        if (mc.thePlayer.onGround) {
            offGroundTicks = 0
        } else {
            offGroundTicks += 1
        }
        if (mc.thePlayer.onGround && mc.thePlayer.motionY > -.2) {
            mc.netHandler.addToSendQueue(
                C04PacketPlayerPosition(
                    (mc.thePlayer.posX + mc.thePlayer.lastTickPosX) / 2,
                    (mc.thePlayer.posY + mc.thePlayer.lastTickPosY) / 2 - 0.0784000015258789,
                    (mc.thePlayer.posZ + mc.thePlayer.lastTickPosZ) / 2,
                    false
                )
            )
            mc.netHandler.addToSendQueue(
                C06PacketPlayerPosLook(
                    (mc.thePlayer.posX + mc.thePlayer.lastTickPosX) / 2,
                    (mc.thePlayer.posY + mc.thePlayer.lastTickPosY) / 2,
                    (mc.thePlayer.posZ + mc.thePlayer.lastTickPosZ) / 2,
                    mc.thePlayer.rotationYaw,
                    mc.thePlayer.rotationPitch,
                    true
                )
            )
            mc.netHandler.addToSendQueue(
                C04PacketPlayerPosition(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY,
                    mc.thePlayer.posZ,
                    false
                )
            )
            mc.netHandler.addToSendQueue(
                C04PacketPlayerPosition(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY - 0.0784000015258789,
                    mc.thePlayer.posZ,
                    false
                )
            )
            mc.netHandler.addToSendQueue(
                C06PacketPlayerPosLook(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY,
                    mc.thePlayer.posZ,
                    mc.thePlayer.rotationYaw,
                    mc.thePlayer.rotationPitch,
                    true
                )
            )
            strafe((getBaseMoveSpeed() * 1.25 * 2).toFloat())
        } else if (offGroundTicks == 1) {
            strafe((getBaseMoveSpeed() * 0.91f).toFloat())
        }
    }
}