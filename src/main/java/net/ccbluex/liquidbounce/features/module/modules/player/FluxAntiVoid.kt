package net.ccbluex.liquidbounce.features.module.modules.player

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold
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
    @EventTarget
    fun onPacket(e: PacketEvent) {
        if (!LiquidBounce.moduleManager.get(Fly::class.java)!!.state && !LiquidBounce.moduleManager.get(Scaffold::class.java)!!.state) {
            if (!packets.isEmpty() && mc.thePlayer.ticksExisted < 100) packets.clear()
            if (e.packet is C03PacketPlayer) {
                if (isInVoid()) {
                    e.cancelEvent()
                    packets.add(e.packet)
                    if (timer.delay(pullbackTime.get().toLong())) {
                        PacketUtils.sendPacketNoEvent(
                            C04PacketPlayerPosition(
                                lastGroundPos[0], lastGroundPos[1] - 1.0,
                                lastGroundPos[2], true
                            )
                        )
                    }
                } else {
                    lastGroundPos[0] = mc.thePlayer.posX
                    lastGroundPos[1] = mc.thePlayer.posY
                    lastGroundPos[2] = mc.thePlayer.posZ
                    if (!packets.isEmpty()) {
                        val var3: Iterator<*> = packets.iterator()
                        alert("[AntiVoid] Release Packets - " + packets.size)
                        while (var3.hasNext()) {
                            val p = var3.next() as C03PacketPlayer
                            PacketUtils.sendPacketNoEvent(p)
                        }
                        packets.clear()
                    }
                    timer.reset()
                }
            }
        }
    }

    @EventTarget
    fun onRevPacket(e: PacketEvent) {
        if (e.packet is S08PacketPlayerPosLook && packets.size > 1) {
            alert("[AntiVoid] Pullbacks Detected, clear packets list!")
            packets.clear()
        }
    }

    override val tag: String
        get() = pullbackTime.get().toString()
}