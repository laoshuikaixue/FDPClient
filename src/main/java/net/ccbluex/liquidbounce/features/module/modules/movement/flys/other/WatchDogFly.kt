package net.ccbluex.liquidbounce.features.module.modules.movement.flys.other

import net.ccbluex.liquidbounce.event.MotionEvent
import net.ccbluex.liquidbounce.event.PacketEvent
import net.ccbluex.liquidbounce.features.module.modules.movement.flys.FlyMode
import net.ccbluex.liquidbounce.utils.MovementUtils
import net.ccbluex.liquidbounce.value.FloatValue
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.network.play.server.S08PacketPlayerPosLook

class WatchDogFly : FlyMode("NWatchDog") {
    private val motionValue = FloatValue("${valuePrefix}Motion", 0f, 0f, 1f)

    private var hasClipped = false
    private var stage = 0f
    private var ticks = 0
    private var doFly = false
    private var x = 0.0
    private var y = 0.0
    private var z = 0.0

    override fun onMotion(event: MotionEvent) {
        mc.thePlayer.cameraYaw = 0.05f.also { mc.thePlayer.cameraPitch = it }
        mc.thePlayer.posY = y
        if (mc.thePlayer.onGround && stage == 0f) {
            mc.thePlayer.motionY = 0.09
        }
        stage++
        if (mc.thePlayer.onGround && stage > 2 && !hasClipped) {
            mc.thePlayer.sendQueue.addToSendQueue(
                C04PacketPlayerPosition(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY - 0.15,
                    mc.thePlayer.posZ,
                    false
                )
            )
            mc.thePlayer.sendQueue.addToSendQueue(
                C04PacketPlayerPosition(
                    mc.thePlayer.posX,
                    mc.thePlayer.posY + 0.15,
                    mc.thePlayer.posZ,
                    true
                )
            )
            hasClipped = true
        }
        if (doFly) {
            mc.thePlayer.motionY = 0.0
            mc.thePlayer.onGround = true
            mc.timer.timerSpeed = 2F
        } else {
            MovementUtils.setSpeed3(0)
            mc.timer.timerSpeed = 5F
        }
    }
    override fun onEnable() {
        doFly = false
        ticks = 0
        stage = 0f
        x = mc.thePlayer.posX
        y = mc.thePlayer.posY
        z = mc.thePlayer.posZ
        hasClipped = false
        super.onEnable()
    }

    override fun onDisable() {
        mc.timer.timerSpeed = 1F
        super.onDisable()
    }


    override fun onPacket(event: PacketEvent) {
        val packet = event.packet

        if (packet is S08PacketPlayerPosLook) {
            val s08 = packet
            y = s08.y
            doFly = true
        }
    }
}