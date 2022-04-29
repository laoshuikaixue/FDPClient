package net.ccbluex.liquidbounce.features.module.modules.movement.flys.other

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.event.UpdateEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.script.api.global.Chat.alert
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.MovementUtils.isMoving
import net.ccbluex.liquidbounce.utils.MovementUtils.strafe
import net.ccbluex.liquidbounce.utils.PacketUtils.sendPacketNoEvent
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition

class Vulcan3Fly : FlyMode("Vulcan3") {
    private var moveSpeed = 0.0
    private var FlyActive = false
    private var vulcanDamaged = false
    private var flyup = false
    
    override fun onEnable() {
        flyup = false
        moveSpeed = 0.75
        if (mc.thePlayer.onGround) {
            sendPacketNoEvent(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 4, mc.thePlayer.posZ, false))
            sendPacketNoEvent(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false))
            sendPacketNoEvent(C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true))
            vulcanDamaged = true
            if (vulcanDamaged) {
                mc.thePlayer.setPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.42, mc.thePlayer.posZ)
            }
            FlyActive = true
            LiquidBounce.hud.addNotification(Notification("VulCanFly", "Successfully", NotifyType.SUCCESS, 1000))
            alert("[DEBUG] VCliped & Damaged")
        }
    }

    override fun onUpdate(event: UpdateEvent) {
        mc.thePlayer.capabilities.isFlying = false
        if (FlyActive && vulcanDamaged) {
            mc.thePlayer.motionY = 0.0
            mc.thePlayer.motionX = 0.0
            mc.thePlayer.motionZ = 0.0
            if (!isMoving()) moveSpeed = 0.25
            if (moveSpeed > 0.25) {
                moveSpeed -= moveSpeed / 159.0
            }
            strafe(moveSpeed as Float)
            if (mc.gameSettings.keyBindJump.isKeyDown) mc.thePlayer.motionY += 0.5
            if (mc.gameSettings.keyBindSneak.isKeyDown) mc.thePlayer.motionY -= 0.5
        }
    }

    override fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is C03PacketPlayer) {
            packet.onGround = true
        }
        event.cancelEvent()
    }
}