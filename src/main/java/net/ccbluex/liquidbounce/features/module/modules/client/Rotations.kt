/*
 * FDPClient Hacked Client
 * A free open source mixin-based injection hacked client for Minecraft using Minecraft Forge by LiquidBounce.
 * https://github.com/laoshuikaixue/FDPClient
 */
package net.ccbluex.liquidbounce.features.module.modules.client

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.EventTarget
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.Module
import net.ccbluex.liquidbounce.features.module.ModuleCategory
import net.ccbluex.liquidbounce.features.module.ModuleInfo
import net.ccbluex.liquidbounce.features.module.modules.combat.BowAimbot
import net.ccbluex.liquidbounce.features.module.modules.combat.KillAura
import net.ccbluex.liquidbounce.features.module.modules.exploit.Disabler
import net.ccbluex.liquidbounce.features.module.modules.movement.Fly
import net.ccbluex.liquidbounce.features.module.modules.world.ChestAura
import net.ccbluex.liquidbounce.features.module.modules.world.Fucker
import net.ccbluex.liquidbounce.features.module.modules.world.Scaffold
import net.ccbluex.liquidbounce.value.BoolValue
import net.ccbluex.liquidbounce.value.ListValue
import net.minecraft.network.play.client.C03PacketPlayer

@ModuleInfo(name = "Rotations", category = ModuleCategory.CLIENT, canEnable = false)
object Rotations : Module() {
    val headValue = BoolValue("Head", true)
    val bodyValue = BoolValue("Body", true)
    val fixedValue = ListValue("SensitivityFixed", arrayOf("None", "Old", "New"), "New")
//    val rotationHumanizeType=EaseUtils.getEnumEasingList("HumanizeType")
//    val rotationHumanizeOrder=EaseUtils.getEnumEasingOrderList("HumanizeOrder")
    val nanValue = BoolValue("NaNCheck", true)

    private fun getState(module: Class<*>) = LiquidBounce.moduleManager.getModule(module.toString())!!.state
    private fun shouldRotate(): Boolean {
        val killAura = LiquidBounce.moduleManager.getModule(KillAura::class.java) as KillAura
        val disabler = LiquidBounce.moduleManager.getModule(Disabler::class.java) as Disabler
        return getState(Scaffold::class.java) ||
                (getState(KillAura::class.java) && killAura.target != null) ||
                (getState(Disabler::class.java) && disabler.canRenderInto3D) ||
                getState(BowAimbot::class.java) || getState(Fucker::class.java) ||
                getState(ChestAura::class.java) || getState(Fly::class.java)
    }

    private var playerYaw: Float? = null
    @EventTarget
    fun onPacket(event: PacketEvent) {
        val packet = event.packet
        if (packet is C03PacketPlayer.C06PacketPlayerPosLook || packet is C03PacketPlayer.C05PacketPlayerLook) {
            playerYaw = (packet as C03PacketPlayer).yaw
            mc.thePlayer.renderYawOffset = packet.getYaw()
            mc.thePlayer.rotationYawHead = packet.getYaw()
        } else {
            if (playerYaw != null)
                mc.thePlayer.renderYawOffset = this.playerYaw!!
            mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset
        }
    }

//    fun apply(value: Double):Double{
//        return EaseUtils.apply(toEnumType(), toEnumOrder(),value)
//    }
//
//    fun toEnumType():EaseUtils.EnumEasingType{
//        return EaseUtils.EnumEasingType.valueOf(rotationHumanizeType.get().uppercase())
//    }
//
//    fun toEnumOrder():EaseUtils.EnumEasingOrder{
//        return EaseUtils.EnumEasingOrder.valueOf(rotationHumanizeOrder.get().uppercase())
//    }
}
