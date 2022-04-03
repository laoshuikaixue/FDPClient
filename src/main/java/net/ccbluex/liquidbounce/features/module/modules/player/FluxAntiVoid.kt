package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketReceiveEvent
import net.ccbluex.liquidbounce.event.PacketSendEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.utils.ClientUtils
import net.ccbluex.liquidbounce.utils.MoveUtils
import net.ccbluex.liquidbounce.utils.PacketUtils
import net.ccbluex.liquidbounce.utils.timer.TimeHelper
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.server.S08PacketPlayerPosLook

@ModuleInfo(name = "FluxAntiVoid", category = ModuleCategory.PLAYER)
class FluxAntiVoid : Module() {
    private val pullbackTime = FloatValue("Pullback Time", 800F, 800F, 1800F)
    var timer: TimeHelper = TimeHelper()
    var lastGroundPos = DoubleArray(3)
    var packets = ArrayList<C03PacketPlayer>()
    @EventTarget
    open fun isInVoid(): Boolean {
        for (i in 0..128) {
            if (MoveUtils.isOnGround(i.toDouble())) {
                return false
            }
        }
        return true
    }

    fun onPacket(e: PacketSendEvent) {
        if (!packets.isEmpty() && mc.thePlayer.ticksExisted < 100) packets.clear()
        if (e.getPacket() is C03PacketPlayer) {
            val packet = e.getPacket() as C03PacketPlayer
            if (isInVoid()) {
                e.setCancelled(true)
                packets.add(packet)
                if (timer.isDelayComplete(pullbackTime.get().toDouble())) {
                    ClientUtils.displayChatMessage("Send Packets")
                    PacketUtils.sendPacketNoEvent(
                        C04PacketPlayerPosition(
                            lastGroundPos[0], lastGroundPos[1] - 1,
                            lastGroundPos[2], true
                        )
                    )
                }
            } else {
                lastGroundPos[0] = mc.thePlayer.posX
                lastGroundPos[1] = mc.thePlayer.posY
                lastGroundPos[2] = mc.thePlayer.posZ
                if (!packets.isEmpty()) {
                    ClientUtils.displayChatMessage("Release Packets - " + packets.size)
                    for (p in packets) PacketUtils.sendPacketNoEvent(p)
                    packets.clear()
                }
                timer.reset()
            }
        }
    }

    @EventTarget
    fun onRevPacket(e: PacketReceiveEvent) {
        if (e.getPacket() is S08PacketPlayerPosLook && packets.size > 1) {
            ClientUtils.displayChatMessage("Pullbacks Detected, clear packets list!")
            packets.clear()
        }
    }
}