package net.ccbluex.liquidbounce.features.module.modules.movement.flys.other

import net.ccbluex.liquidbounce.LiquidBounce
import net.ccbluex.liquidbounce.event.*
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.Notification
import net.ccbluex.liquidbounce.ui.client.hud.element.elements.NotifyType
import net.ccbluex.liquidbounce.utils.MovementUtils.getBaseMoveSpeed
import net.ccbluex.liquidbounce.utils.MovementUtils.strafe
import net.ccbluex.liquidbounce.value.BoolValue
import net.minecraft.network.play.client.C03PacketPlayer
import net.minecraft.network.play.client.C0CPacketInput
import net.minecraft.network.play.server.S08PacketPlayerPosLook
import net.minecraft.potion.Potion

class WatchDogFly2 : FlyMode("WatchDog2") {
    private val pulsiveTroll = BoolValue("PulsiveTroll", true)
    private val fakeNoMoveValue = BoolValue("FakeNoMove", true)

    private var wdState = 0
    private var wdTick = 0
    private val startY = 0.0

    fun coerceAtMost(value: Double, max: Double): Float {
        return Math.min(value, max).toFloat()
    }

    override fun onEnable() {
        if (mc.thePlayer == null) return
        val x = mc.thePlayer.posX
        val y = mc.thePlayer.posY
        val z = mc.thePlayer.posZ
        wdState = 0
        wdTick = 0
        if (mc.thePlayer.onGround) mc.thePlayer.setPosition(x, y + 0.1, z)
    }

    override fun onUpdate(event: UpdateEvent) {
        if (wdState == 3) {
            mc.timer.timerSpeed = 1f
            mc.thePlayer.motionY = 0.0001
            strafe((getBaseMoveSpeed() * if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) 0.8 else 0.75).toFloat())
        } else if (wdState == 2) {
            mc.timer.timerSpeed = 1.5f
            mc.thePlayer.motionY = 0.0001
            strafe((getBaseMoveSpeed() * if (mc.thePlayer.isPotionActive(Potion.moveSpeed)) 0.81 else 0.77).toFloat())
        }
    }

    override fun onMotion(event: MotionEvent) {
        if (event.eventState === EventState.POST && pulsiveTroll.get() && wdState >= 2 && mc.thePlayer.ticksExisted % 2 == 0) mc.netHandler.addToSendQueue(
            C0CPacketInput(
                coerceAtMost(
                    mc.thePlayer.moveStrafing.toDouble(), 0.98
                ),
                coerceAtMost(mc.thePlayer.moveForward.toDouble(), 0.98),
                mc.thePlayer.movementInput.jump,
                mc.thePlayer.movementInput.sneak
            )
        )
    }
    override fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is S08PacketPlayerPosLook) {
            if (wdState == 1) {
                wdState = 2
                LiquidBounce.hud.addNotification(Notification("NCPTestFly","Activated.", NotifyType.SUCCESS, 3000))
            } else if (wdState == 2) {
                wdState = 3
                LiquidBounce.hud.addNotification(Notification("NCPTestFly","Flagged.", NotifyType.INFO, 3000))
            }
        }

        if (packet is C03PacketPlayer) {
            val packetPlayer = packet
            if (wdState == 0 && packetPlayer.onGround) {
                packetPlayer.y -= 0.187
                wdState = 1
            } else {
                packetPlayer.y = startY
                if (fakeNoMoveValue.get()) packetPlayer.setMoving(false)
            }
        }
    }

    override fun onMove(event: MoveEvent) {
        if (wdState < 2) event.zeroXZ()
    }
}